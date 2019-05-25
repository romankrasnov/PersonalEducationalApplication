package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CompressUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CustomSubsamplingScaleImageView;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.Adapters.CustomFinalPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters.CreateTestFragmentPresenter.STATUS_LOADING;


@SuppressLint("ValidFragment")
public class AnswerContentFragment extends Fragment implements
        CustomFinalPageAdapter.ViewModeChangeListener {

    private static final String PARAM_ID = "id";
    private static final String PARAM_CONTENT = "content";
    private static final String PARAM_TYPE = "type";
    private static final String VIEW_MODE = "view_mode";

    private final ICreateTestFragmentMVPprovider.IPresenter presenter;

    Unbinder unbinder;
    @BindView(R.id.scrollLayout)
    LinearLayout scrollLayout;
    @BindView(R.id.answerItemScrollView)
    ScrollView answerItemScrollView;
    @BindView(R.id.rootAnswerFragmentContainer)
    FrameLayout rooAnswerFragmentContainer;
    @BindView(R.id.imageView)
    CustomSubsamplingScaleImageView imageView;
    @BindView(R.id.contentView)
    TextView contentView;
    @BindView(R.id.card)
    CardView card;
    @BindView(R.id.cardChildContainer)
    FrameLayout cardChildContainer;

    private String id;
    private String content;
    private int type;
    private float mMotionDownY;
    private boolean isFullScreenMode;


    @SuppressLint("ValidFragment")
    public AnswerContentFragment(ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter) {
        this.presenter = createTestFragmentPresenter;
    }


    public static AnswerContentFragment newInstance(ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter, String id, String content, int type, boolean isFullScreenMode) {
        AnswerContentFragment fragment = new AnswerContentFragment(createTestFragmentPresenter);
        Bundle args = new Bundle();
        args.putString(PARAM_ID, id);
        args.putString(PARAM_CONTENT, content);
        args.putInt(PARAM_TYPE, type);
        args.putBoolean(VIEW_MODE, isFullScreenMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(PARAM_ID);
            content = getArguments().getString(PARAM_CONTENT);
            type = getArguments().getInt(PARAM_TYPE);
            isFullScreenMode = getArguments().getBoolean(VIEW_MODE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewModeChanged(isFullScreenMode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewModeChanged(boolean isFullScreenMode) {
        if (isFullScreenMode) {
            setFullScreenViews();
        } else {
            setSmallScreenViews();
        }
        if (getArguments() != null) {
            getArguments().putBoolean(VIEW_MODE, isFullScreenMode);
        }
    }

    public void resetTransition() {
        card.setTranslationY(0);
    }

    public void animate(MotionEvent e2) {
        card.setTranslationY(e2.getRawY() - mMotionDownY);
    }

    public void calculateScroll(MotionEvent e) {
        mMotionDownY = e.getRawY() - card.getTranslationY();
    }


    public void slideView(MotionEvent event) {
        float diff = mMotionDownY - event.getRawY();
        if (diff < 400) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(card, "translationY", 0);
            animator.setDuration(150);
            animator.setInterpolator(new FastOutLinearInInterpolator());
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(card, "translationY", -2000f);
            animator.setDuration(150);
            animator.setInterpolator(new FastOutLinearInInterpolator());
            animator.start();
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    presenter.onAnswerViewSwipe(id);
                    super.onAnimationEnd(animation);
                }
            });
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    public void setFullScreenViews() {
        switch (type)
        {
            case -1: {
                contentView.setText(content);
                imageView.setOnClickListener(view -> presenter.onAnswerFragmentClick(id));
                break;
            }

            case 0: {
                contentView.setText(content);
                cardChildContainer.removeView(imageView);
                setFullScreenTextListeners();
                break;
            }

            case 1: {
                contentView.setText("");
                cardChildContainer.removeView(imageView);
                cardChildContainer.addView(imageView);
                loadImage(true);
                setFullScreenPhotoListeners();
                break;
            }

            case 2: {
                contentView.setText("");
                cardChildContainer.removeView(imageView);
                cardChildContainer.addView(imageView);
                loadImage(true);
                setFullScreenPhotoListeners();
                break;
            }

            case 3: {
                if(content == null)
                {
                    contentView.setText(STATUS_LOADING);
                } else
                    {
                        contentView.setText(content);
                    }
                cardChildContainer.removeView(imageView);
                setFullScreenTextListeners();
                break;
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setFullScreenTextListeners() {
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
                {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        presenter.onAnswerDoubleTap(id);
                        return super.onDoubleTap(e);
                    }
                }).onTouchEvent(motionEvent);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setFullScreenPhotoListeners() {

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float currentScale = ((float) Math.round(imageView.getScale() * 100))/100;
                float minScale = ((float) Math.round(imageView.getMinScale() * 100))/100;
                if(currentScale == minScale)
                {
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                    {
                        presenter.onAnswerFragmentUp(id, motionEvent);
                    }
                    return new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
                    {
                        @Override
                        public boolean onDown(MotionEvent e) {
                            presenter.onAnswerDown(id, e);
                            return super.onDown(e);
                        }

                        @Override
                        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                            presenter.onAnswerScroll(id, e2);
                            return super.onScroll(e1,e2,distanceX,distanceY);
                        }
                    }).onTouchEvent(motionEvent);
                } else
                    {
                    return imageView.onTouchEvent(motionEvent);
                }
            }
        });
    }


    public void setSmallScreenViews() {
        switch (type)
        {
            case -1: {
                contentView.setText(content);
                imageView.setOnClickListener(view -> presenter.onAnswerFragmentClick(id));
                break;
            }

            case 0: {
                contentView.setText(content);
                setSmallScreenListeners();
                break;
            }

            case 1: {
                contentView.setText("");
                loadImage(false);
                setSmallScreenListeners();
                break;
            }

            case 2: {
                contentView.setText("");
                loadImage(false);
                setSmallScreenListeners();
                break;
            }

            case 3: {
                if(content == null)
                {
                    contentView.setText(STATUS_LOADING);
                }else
                    {
                        contentView.setText(content);
                    }
                setSmallScreenListeners();
                break;
            }
        }
    }

    private void loadImage(boolean isZoomEnabled) {
        imageView.setZoomEnabled(isZoomEnabled);
        imageView.setDrawingCacheEnabled(true);
        new CompressUtil(getContext())
                .getBitmap(content)
                .doOnSuccess(bitmap -> imageView.setImage(ImageSource.cachedBitmap(bitmap)))
                .subscribe();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setSmallScreenListeners() {
        contentView.setOnTouchListener(null);
        cardChildContainer.removeView(imageView);
        cardChildContainer.addView(imageView);
        imageView.setOnClickListener(view -> presenter.onAnswerFragmentClick(id));
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    presenter.onAnswerFragmentUp(id, motionEvent);
                }

                return new GestureDetector(AnswerContentFragment.this.getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        presenter.onAnswerDown(id, e);
                        return super.onDown(e);
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        presenter.onAnswerScroll(id, e2);
                        return super.onScroll(e1,e2,distanceX,distanceY);
                    }
                }).onTouchEvent(motionEvent);
            }
        });
    }

}
