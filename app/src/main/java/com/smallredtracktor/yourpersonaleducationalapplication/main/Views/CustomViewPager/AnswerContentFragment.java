package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters.CreateTestFragmentPresenter.STATUS_LOADING;


@SuppressLint("ValidFragment")
public class AnswerContentFragment extends Fragment {

    private static final String PARAM_ID = "id";
    private static final String PARAM_CONTENT = "content";
    private static final String PARAM_TYPE = "type";

    private final ICreateTestFragmentMVPprovider.IPresenter presenter;

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
        return inflater.inflate(R.layout.fragment_answer_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView contentView = view.findViewById(R.id.contentView);

        contentView.setOnLongClickListener(v -> {
            presenter.onAnswerLongClick(id);
            return false;
        });

        switch (type)
        {
            case -1:
            {
                contentView.setText(content);
                contentView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                break;
            }

            case 0 :
            {
                contentView.setText(content);
                contentView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                break;
            }

            case 1 :
            {
                if (content == null)
                {
                    contentView.setText(STATUS_LOADING);
                } else {
                    contentView.setText("");
                    contentView.setBackgroundDrawable(Objects.requireNonNull(Drawable.createFromPath(content)));
                    contentView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                }
                break;

            }

            case 2 :
            {
                if (content == null)
                {
                    contentView.setText(STATUS_LOADING);
                } else {
                    contentView.setText("");
                    contentView.setBackgroundDrawable(Objects.requireNonNull(Drawable.createFromPath(content)));
                    contentView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                }
                break;
            }

            case 3 :
            {
                if (content == null)
                {
                    contentView.setText(STATUS_LOADING);
                } else {
                    contentView.setText(content);
                    contentView.setOnClickListener(v -> presenter.onAnswerFragmentInteraction(id));
                }

                break;
            }
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

}
