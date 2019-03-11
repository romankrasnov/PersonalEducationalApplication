package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener;
import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.ApplicationPhoto;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.SwipeUtils.OnSwipeTouchListener;
import com.smallredtracktor.yourpersonaleducationalapplication.root.App;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateTestFragment extends Fragment implements
        ICreateTestFragmentMVPprovider.IFragment,
        ActivityCompat.PermissionCompatDelegate {

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


    @Inject
    ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter;


    private OnFragmentInteractionListener mListener;
    private final int requestCode = 200;

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

        App.getComponent()
                .plusCreateTestComponent(new CreateTestModule())
                .inject(this);

        view.setOnTouchListener(new OnSwipeTouchListener(this.getContext()) {
            public void onSwipeTop() {
                createTestFragmentPresenter.onSwipeTop();
            }

            public void onSwipeRight() {
                createTestFragmentPresenter.onSwipeRight();
            }

            public void onSwipeLeft() {
                createTestFragmentPresenter.onSwipeLeft();

            }

            public void onSwipeBottom() {
                createTestFragmentPresenter.onSwipeBottom();
            }

        });

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTestFragmentPresenter.onAddQuestionClick();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        createTestFragmentPresenter.setView(this);
        super.onResume();
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

    @Override
    public void setCounterTextView(String s) {
        counterTicketsTextView.setText(s);
    }

    @Override
    public void addSubjectToQuestionStack(View v) {
        questionStackLayout.addView(v);
    }

    @Override
    public void addSubjectToAnswerStack(View v) {
        answerStackLayout.addView(v);
    }

    @Override
    public void removeSubjectFromQuestionStack(int position) {
        questionStackLayout.removeViewAt(position);
    }

    @Override
    public void removeSubjectFromAnswerStack(int position) {
        answerStackLayout.removeViewAt(position);
    }

    @Override
    public void showPhotoFragment(ApplicationPhoto photo) {

    }

    @Override
    public void showTextFragment(String text) {

    }

    @Override
    public void destroyPhotoFragment() {

    }

    @Override
    public void destroyTextFragment() {

    }

    @Override
    public void destroyFragment() {

    }

    @Override
    public void showLeftSwipeAnimation() {
        Animation leftSwipeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        createTestLayout.startAnimation(leftSwipeAnimation);
    }

    @Override
    public void showRightSwipeAnimation() {
        Animation rightSwipeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        createTestLayout.startAnimation(rightSwipeAnimation);
    }

    @Override
    public void setObjectColour(View v) {

    }

    @Override
    public void showCameraFragment() {
        @SuppressLint("MissingPermission")

                //<--- TODO replace with native camera, CameraFragment not supported
                // TODO resolve bug which provide only ONCE application instance (m.b. AppModule bug --->

        CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder()
                .setCamera(Configuration.CAMERA_FACE_FRONT)
                .build());
        getChildFragmentManager().beginTransaction()
                .replace(R.id.createTestLayout, cameraFragment)
                .commit();

        cameraFragment.setResultListener(new CameraFragmentResultListener() {
            @Override
            public void onVideoRecorded(String filePath) {
                createTestFragmentPresenter.onVideoRecorded();
            }

            @Override
            public void onPhotoTaken(byte[] bytes, String filePath) {
                createTestFragmentPresenter.onPhotoTaken(bytes,filePath);
            }
        });
    }

    @Override
    public void showGallery() {

    }

    @Override
    public void showErrorWhileNetworkingMessage() {

    }

    @Override
    public void showErrorWhileTakingPhotoMessage() {

    }

    @Override
    public void showDeleteAlertDialog() {

    }

    @Override
    public void showWhatsSubjectDialog() {

    }

    @Override
    public void showChooseSourceDialog() {

    }

    @Override
    public void resolveCameraPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.setPermissionCompatDelegate(this);
            requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, requestCode);
        }
    }

    @Override
    public boolean requestPermissions(@NonNull Activity activity, @NonNull String[] strings, int i) {
        return true;
    }

    @Override
    public boolean onActivityResult(@NonNull Activity activity, int reqCode, int resCode, @Nullable Intent intent) {
        createTestFragmentPresenter.onPhotoPermissionCompatResult(reqCode, resCode);
        return false;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCreateTestFragmentInteraction(Uri uri);
    }
}
