package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.PhotoDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.TextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.MyOwnPageAdapter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.MyViewPager;
import com.smallredtracktor.yourpersonaleducationalapplication.root.App;

import java.util.Objects;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


import static android.app.Activity.RESULT_OK;



@SuppressLint("ValidFragment")
public class CreateTestFragment extends Fragment implements
        ICreateTestFragmentMVPprovider.IFragment {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PICK_IMAGE = 2;
    public static final String STUB_PARAM_ID = "new";
    private static final String OUTCOME_PARAM = "outcome_param";
    public static final String STUB_PARAM = "CLICK TO ADD ANSWER";
    private static final String APP_ITEM_TYPE = "type";
    private static final String APP_ITEM_IS_QUESTION = "isQuestion";
    private static final String FILE_PATH = "file_path";
    @BindView(R.id.questionTextView)
    TextView questionTextView;
    @BindView(R.id.answerImageView)
    ImageView questionImageView;
    private Bundle options;
    private String outcomeParam;

    @BindView(R.id.ticketCounterTextView)
    TextView ticketCounterTextView;

    @BindView(R.id.viewPager)
    MyViewPager viewPager;


    @Inject
    ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter;
    @Inject
    ChooseSourceDialog chooseSourceDialog;
    @Inject
    PhotoDialog photoDialog;
    @Inject
    TextDialog dialog;


    private MyOwnPageAdapter adapter;
    private Unbinder unbinder;


    public CreateTestFragment() {
    }

    public static CreateTestFragment newInstance(String outcome) {
        CreateTestFragment fragment = new CreateTestFragment();
        Bundle args = new Bundle();
        args.putString(OUTCOME_PARAM, outcome);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            outcomeParam = getArguments().getString(OUTCOME_PARAM);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        App.get(Objects.requireNonNull(getContext()))
                .getComponent()
                .plusCreateTestComponent(new CreateTestModule(getContext()))
                .inject(this);
        questionTextView.setOnClickListener(v -> createTestFragmentPresenter.onAddQuestionClick());
        questionTextView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(STUB_PARAM_ID));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MyOwnPageAdapter(getChildFragmentManager());
        adapter.setViewPager(viewPager);
        adapter.setPresenter(createTestFragmentPresenter);
        adapter.addFirstItem();
        viewPager.setOffscreenPageLimit(50);
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        createTestFragmentPresenter.setView(this);
        super.onResume();
        createTestFragmentPresenter.onViewResumed(outcomeParam);
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
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setCounterTextView(String s) {
        ticketCounterTextView.setText(s);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void deleteQuestion() {
        questionTextView.setText("CLICK TO ADD QUESTION");
        questionImageView.setImageBitmap(null);
        questionTextView.setOnClickListener(v -> createTestFragmentPresenter.onAddQuestionClick());
        questionTextView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(STUB_PARAM_ID));
    }

    @Override
    public void setTextQuestion(String id, int type, String content) {
        questionTextView.setText(content);
        questionImageView.setImageBitmap(null);
        questionTextView.setOnClickListener(v -> createTestFragmentPresenter.onQuestionPressed(id));
        questionTextView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(id));
    }

    @Override
    public void setPhotoQuestion(String id, int type, Bitmap content) {
        Glide.with(this)
                .load(content)
                .into(questionImageView);
        questionTextView.setText("");
        questionTextView.setOnClickListener(v -> createTestFragmentPresenter.onQuestionPressed(id));
        questionTextView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(id));
    }

    @Override
    public void setCurrentAnswer(String id, int type, String content) {
        adapter.setItem(id, type, content);
    }


    @Override
    public void addNewAnswer() {
        adapter.addItem(STUB_PARAM_ID, STUB_PARAM);
    }

    @Override
    public void removeAnswer(String id) {
        adapter.removeItem(id);
    }


    @Override
    public void showPhotoFragment(String id, String value, int type, boolean isQuestion) {
        photoDialog.setDialogParams(id, value, type, isQuestion);
        photoDialog.show();
    }

    @Override
    public void showTextFragment(String id, String text, int type, boolean isQuestion) {
        dialog.setDialogParams(text, id, type, isQuestion);
        dialog.show(getChildFragmentManager(), null);
    }


    @Override
    public void showCameraFragment(Intent intent, int type, boolean isQuestion, String path) {
        options = new Bundle();
        options.putInt(APP_ITEM_TYPE, type);
        options.putBoolean(APP_ITEM_IS_QUESTION, isQuestion);
        options.putString(FILE_PATH, path);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    @Override
    public void showGallery(int type, boolean isQuestion) {
        Intent intent = new Intent();
        intent.setType("image/*");
        options = new Bundle();
        options.putInt(APP_ITEM_TYPE, type);
        options.putBoolean(APP_ITEM_IS_QUESTION, isQuestion);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    @Override
    public void showWhatsSubjectDialog() {

    }

    @Override
    public void showChooseSourceDialog(boolean isQuestion) {
        chooseSourceDialog.setIsQuestion(isQuestion);
        chooseSourceDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void resolveCameraPermission() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA)
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            createTestFragmentPresenter.onPhotoTaken(options.getString(FILE_PATH),
                    options.getInt(APP_ITEM_TYPE),
                    options.getBoolean(APP_ITEM_IS_QUESTION));

        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri contentURI = data.getData();
            createTestFragmentPresenter.onGalleryResult(contentURI,
                    options.getInt(APP_ITEM_TYPE),
                    options.getBoolean(APP_ITEM_IS_QUESTION));

        }
    }
}
