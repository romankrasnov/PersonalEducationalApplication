package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.TextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.PhotoIntent;
import com.smallredtracktor.yourpersonaleducationalapplication.root.App;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


@SuppressLint("ValidFragment")
public class CreateTestFragment extends Fragment implements
        ICreateTestFragmentMVPprovider.IFragment {
    private static final int REQUEST_TAKE_PHOTO = 1;
    @BindView(R.id.ticketCounterTextView)
    TextView ticketCounterTextView;
    @BindView(R.id.addQuestionButton)
    Button addQuestionButton;
    @BindView(R.id.answersViewPager)
    ViewPager answersViewPager;
    @BindView(R.id.frameContainer)
    FrameLayout frameContainer;
    private String mPath;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    @Inject
    ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter;

    private ViewPager pager;



    public CreateTestFragment() {
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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_test, container, false);
        ButterKnife.bind(this, view);
        App.get(getContext())
                .getComponent()
                .plusCreateTestComponent(new CreateTestModule(getContext()))
                .inject(this);
        addQuestionButton.setOnClickListener(v -> createTestFragmentPresenter.onAddQuestionClick());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pager = view.findViewById(R.id.answersViewPager);
        pager.setOffscreenPageLimit(100);
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onResume() {
        createTestFragmentPresenter.setView(this);
        super.onResume();
        createTestFragmentPresenter.onViewResumed(mParam1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        createTestFragmentPresenter = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        createTestFragmentPresenter.rxUnsubscribe();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setCounterTextView(String s) {
        ticketCounterTextView.setText(s);
    }

    @Override
    public void addQuestion(String id) {
        addQuestionButton.setOnClickListener(v -> createTestFragmentPresenter.onQuestionPressed(id));
    }

    @Override
    public void setQuestion(String value) {
        addQuestionButton.setText(value);
    }

    @Override
    public void setAnswer(String id, String param) {

    }


    @Override
    public void addAnswer() {
    }

    @Override
    public void removeAnswer(String position) {

    }


    @Override
    public void showPhotoFragment(Bitmap bitmap) {

    }

    @Override
    public void showTextFragment(String text) {
        TextDialog dialog = new TextDialog(createTestFragmentPresenter);
        dialog.setDialogText(text);
        dialog.show(getChildFragmentManager(), null);
    }


    @Override
    public void destroyFragment() {
    }


    @Override
    public void showCameraFragment() {
        Intent takePictureIntent = PhotoIntent.getInstance(getContext());
        mPath = PhotoIntent.getPath();
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    @Override
    public void showGallery() {

    }


    @Override
    public void showDeleteAlertDialog() {

    }

    @Override
    public void showWhatsSubjectDialog() {

    }

    @Override
    public void showChooseSourceDialog() {
        DialogFragment dialog = new ChooseSourceDialog(createTestFragmentPresenter);
        dialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void resolveCameraPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            int requestCode = 200;
            requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
}
