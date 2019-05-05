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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CompressUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters.CreateTestFragmentPresenter.STATUS_LOADING;


@SuppressLint("ValidFragment")
public class AnswerContentFragment extends Fragment {

    private static final String PARAM_ID = "id";
    private static final String PARAM_CONTENT = "content";
    private static final String PARAM_TYPE = "type";

    private final ICreateTestFragmentMVPprovider.IPresenter presenter;
    @BindView(R.id.contentView)
    TextView contentView;
    @BindView(R.id.imageView)
    ImageView imageView;
    Unbinder unbinder;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (type) {
            case -1: {
                imageView.setOnLongClickListener(v -> {
                    presenter.onAnswerLongClick(id);
                    return false;
                });
                contentView.setText(content);
                imageView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                break;
            }

            case 0: {
                imageView.setOnLongClickListener(v -> {
                    presenter.onAnswerLongClick(id);
                    return false;
                });
                contentView.setText(content);
                imageView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
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
                imageView.setOnLongClickListener(v -> {
                    presenter.onAnswerLongClick(id);
                    return false;
                });
                if (content == null) {
                    contentView.setText(STATUS_LOADING);
                } else {
                    contentView.setText(content);
                    imageView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                }

                break;
            }
        }
    }

    private void loadImageIfNecessary() {
        if (content == null) {
            contentView.setText(STATUS_LOADING);
        } else {
            new CompressUtil(getContext())
                    .getBitmap(content)
                    .doOnSuccess(bitmap -> Glide.with(this)
                    .load(new File(content))
                    .into(imageView))
                    .subscribe();
            contentView.setText("");
            imageView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
            imageView.setOnLongClickListener(v -> {
                presenter.onAnswerLongClick(id);
                return false;
            });
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
}
