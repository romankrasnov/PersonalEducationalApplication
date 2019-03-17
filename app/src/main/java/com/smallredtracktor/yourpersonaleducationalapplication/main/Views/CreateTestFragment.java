package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.ApplicationPhoto;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.PhotoIntent;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.SwipeUtils.OnSwipeTouchListener;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;
import com.smallredtracktor.yourpersonaleducationalapplication.root.App;


import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class CreateTestFragment extends Fragment implements
        ICreateTestFragmentMVPprovider.IFragment
   {

       private static final int REQUEST_TAKE_PHOTO = 1;
       private String mPath;

       private int globalTicketCouter = 1;

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


    @Inject
    ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter;

    private OnFragmentInteractionListener mListener;

    private final int requestCode = 200;


    public CreateTestFragment() {
        // Required empty public constructor
    }

       public static CreateTestFragment newInstance(String param1, String param2) {
        CreateTestFragment fragment = new CreateTestFragment();
        return fragment;
    }

             @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_test, container, false);
        ButterKnife.bind(this, view);

        App.get(getContext())
                .plusCreateTestComponent()
                .inject(this);

        view.setOnTouchListener(new OnSwipeTouchListener(this.getContext()) {
            public void onSwipeTop() {
                createTestFragmentPresenter.onSwipeTop();
            }

            public void onSwipeRight() {
                createTestFragmentPresenter.onSwipeRight();
            }

            public void onSwipeLeft() { createTestFragmentPresenter.onSwipeLeft(); }

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
    }

    @Override
    public void showRightSwipeAnimation() {
    }

    @Override
    public void setObjectColour(View v) {
    }

    @Override
    public void showCameraFragment() {
        Intent takePictureIntent =  PhotoIntent.getInstance(getContext());
        mPath = PhotoIntent.mPath;
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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
        DialogFragment dialog = new ChooseSourceDialog();
        dialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void resolveCameraPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.CAMERA},requestCode);
        }
    }


       @Override
       public void onActivityResult(int requestCode, int resultCode, Intent data) {
           super.onActivityResult(requestCode, resultCode, data);
           if (resultCode == RESULT_OK) {
               if (requestCode == REQUEST_TAKE_PHOTO) {
                   createTestFragmentPresenter.onPhotoTaken(mPath);
               }
           }
           if (resultCode == RESULT_CANCELED) {
               if (requestCode == REQUEST_TAKE_PHOTO) {
                   createTestFragmentPresenter.onPhotoTakingCancelled();
               }
           }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCreateTestFragmentInteraction(Uri uri);
    }
}
