package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
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
    private static final String IS_FULLSCREEN_MODE = "is_fullscreen_mode";

    private final ICreateTestFragmentMVPprovider.IPresenter presenter;

    Unbinder unbinder;
    @BindView(R.id.scrollLayout)
    LinearLayout scrollLayout;
    @BindView(R.id.answerItemScrollView)
    ScrollView answerItemScrollView;
    @BindView(R.id.rooAnswerFragmentContainer)
    FrameLayout rooAnswerFragmentContainer;
    @BindView(R.id.imageView)
    CustomSubsamplingScaleImageView imageView;
    @BindView(R.id.contentView)
    TextView contentView;


    private String id;
    private String content;
    private int type;
    private boolean isFullScreenMode = false;


    @SuppressLint("ValidFragment")
    public AnswerContentFragment(ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter) {
        this.presenter = createTestFragmentPresenter;
    }


    public static AnswerContentFragment newInstance(ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter, String id, String content, int type) {
        AnswerContentFragment fragment = new AnswerContentFragment(createTestFragmentPresenter);
        Bundle args = new Bundle();
        args.putString(PARAM_ID, id);
        args.putString(PARAM_CONTENT, content);
        args.putInt(PARAM_TYPE, type);
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
            if (savedInstanceState != null) {
                isFullScreenMode = savedInstanceState.getBoolean(IS_FULLSCREEN_MODE);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        rooAnswerFragmentContainer.removeView(answerItemScrollView);
        rooAnswerFragmentContainer.removeView(imageView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewParams();
    }

    private void setViewParams() {

        switch (type) {
            case -1: {
                rooAnswerFragmentContainer.addView(answerItemScrollView);
                rooAnswerFragmentContainer.removeView(imageView);
                contentView.setText(content);
                contentView.setOnClickListener(v -> presenter.onAnswerFragmentClick(id, isFullScreenMode));
                break;
            }
            case 0: {
                rooAnswerFragmentContainer.addView(answerItemScrollView);
                contentView.setText(content);
                setInteractionListeners(answerItemScrollView);
                break;
            }

            case 1: {
                loadImageIfNecessary();
                break;
            }

            case 2: {
                loadImageIfNecessary();
                break;
            }

            case 3: {
                rooAnswerFragmentContainer.addView(answerItemScrollView);
                if (content == null) {
                    contentView.setText(STATUS_LOADING);
                    setInteractionListeners(answerItemScrollView);
                } else {
                    contentView.setText(content);
                    setInteractionListeners(answerItemScrollView);
                }
                break;
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setInteractionListeners(View view) {
        final GestureDetector gd = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                presenter.onAnswerFragmentClick(id, isFullScreenMode);
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                presenter.onAnswerLongClick(id);
                super.onLongPress(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                presenter.onAnswerDoubleTap(id);
                return true;
            }
        });
        view.setOnTouchListener((view1, motionEvent) -> gd.onTouchEvent(motionEvent));
    }


    private void loadImageIfNecessary() {
        if (content == null) {
            rooAnswerFragmentContainer.addView(contentView);
            contentView.setText(STATUS_LOADING);
        } else {
            rooAnswerFragmentContainer.removeView(contentView);
            rooAnswerFragmentContainer.addView(imageView);
            imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
            imageView.setDrawingCacheEnabled(true);
            imageView.setZoomEnabled(isFullScreenMode);
            new CompressUtil(getContext())
                    .getBitmap(content)
                    .doOnSuccess(bitmap ->
                            imageView.setImage(ImageSource.cachedBitmap(bitmap)))
                    .subscribe();
            setInteractionListeners(imageView);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewModeChanged(boolean isFullScreenMode) {
        this.isFullScreenMode = isFullScreenMode;
        if (imageView != null) {
            imageView.setZoomEnabled(isFullScreenMode);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_FULLSCREEN_MODE, isFullScreenMode);
    }
}
