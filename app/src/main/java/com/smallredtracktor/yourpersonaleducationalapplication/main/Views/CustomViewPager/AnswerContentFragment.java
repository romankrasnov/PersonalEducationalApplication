package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters.CreateTestFragmentPresenter.STATUS_LOADING;


@SuppressLint("ValidFragment")
public class AnswerContentFragment extends Fragment implements
        MyOwnPageAdapter.ViewModeChangeListener {

    private static final String PARAM_ID = "id";
    private static final String PARAM_CONTENT = "content";
    private static final String PARAM_TYPE = "type";

    private final ICreateTestFragmentMVPprovider.IPresenter presenter;
    @BindView(R.id.contentView)
    TextView contentView;
    @BindView(R.id.imageView)
    CustomSubsamplingScaleImageView imageView;
    Unbinder unbinder;
    @BindView(R.id.transparentContentView)
    TextView transparentContentView;
    @BindView(R.id.scrollLayout)
    LinearLayout scrollLayout;
    @BindView(R.id.answerItemScrollView)
    ScrollView answerItemScrollView;
    @BindView(R.id.rooAnswerFragmentContainer)
    FrameLayout rooAnswerFragmentContainer;



    private String id;
    private String content;
    private int type;


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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
        imageView.setZoomEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (type) {
            case -1: {
                transparentContentView.setText(content);
                imageView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                break;
            }

            case 0: {
                imageView.setVisibility(View.INVISIBLE);
                transparentContentView.setText("");
                contentView.setText(content);
                contentView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                contentView.setOnLongClickListener(v -> {
                    presenter.onAnswerLongClick(id);
                    return false;
                });
                break;
            }

            case 1: {
                loadImageIfNecessary();
                imageView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                imageView.setOnLongClickListener(v -> {
                    presenter.onAnswerLongClick(id);
                    return false;
                });
                break;
            }

            case 2: {
                loadImageIfNecessary();
                imageView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                imageView.setOnLongClickListener(v -> {
                    presenter.onAnswerLongClick(id);
                    return false;
                });
                break;
            }

            case 3: {
                imageView.setVisibility(View.INVISIBLE);
                if (content == null) {
                    transparentContentView.setText(STATUS_LOADING);
                    transparentContentView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                    transparentContentView.setOnLongClickListener(v -> {
                        presenter.onAnswerLongClick(id);
                        return false;
                    });
                } else {
                    transparentContentView.setText("");
                    contentView.setText(content);
                    contentView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                    contentView.setOnLongClickListener(v -> {
                        presenter.onAnswerLongClick(id);
                        return false;
                    });
                }
                break;
            }
        }
    }


    private void loadImageIfNecessary() {
        if (content == null) {
            transparentContentView.setText(STATUS_LOADING);
        } else {
            imageView.setVisibility(View.VISIBLE);
            new CompressUtil(getContext())
                    .getBitmap(content)
                    .doOnSuccess(bitmap ->
                        imageView.setImage(ImageSource.cachedBitmap(bitmap)))
                    .subscribe();
            contentView.setText("");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewModeChanged(boolean isFullScreenMode) {
        if (!isFullScreenMode) {
            imageView.setZoomEnabled(false);
        } else
            {
                imageView.setZoomEnabled(true);
            }
    }
}
