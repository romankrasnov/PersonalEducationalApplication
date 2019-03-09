package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.SwipeUtils.OnSwipeTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateTestFragment extends Fragment implements
        ICreateTestFragmentMVPprovider.IFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.counterTicketsTextView)
    TextView counterTicketsTextView;
    @BindView(R.id.questionStackLayout)
    LinearLayout questionStackLayout;
    @BindView(R.id.addQuestionButton)
    Button addQuestionButton;
    @BindView(R.id.answerStackLayout)
    LinearLayout answerStackLayout;
    @BindView(R.id.addAnswerButton)
    Button addAnswerButton;
    @BindView(R.id.clearButton)
    Button clearButton;
    @BindView(R.id.doneButton)
    Button doneButton;
    @BindView(R.id.createTestLayout)
    LinearLayout createTestLayout;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int ticketCounter = 0;

    private OnFragmentInteractionListener mListener;

    public CreateTestFragment() {
        // Required empty public constructor
    }


    public static CreateTestFragment newInstance(String param1, String param2) {
        CreateTestFragment fragment = new CreateTestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_test, container, false);
        ButterKnife.bind(this, view);


        view.setOnTouchListener(new OnSwipeTouchListener(this.getContext()) {
            public void onSwipeTop() {
                counterTicketsTextView.setText("Top");
            }

            public void onSwipeRight() {
                counterTicketsTextView.setText("Right");
                //TODO добить анимацию, в этих методах презентер подпишется, также вью должен реализовать методы интерфейса
                //завязаные на логике представления пользователю
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
                createTestLayout.startAnimation(animation);
            }

            public void onSwipeLeft() {
                counterTicketsTextView.setText("Left");

            }

            public void onSwipeBottom() {
                counterTicketsTextView.setText("Bottom");
            }

        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCreateTestFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCreateTestFragmentInteraction(Uri uri);
    }
}
