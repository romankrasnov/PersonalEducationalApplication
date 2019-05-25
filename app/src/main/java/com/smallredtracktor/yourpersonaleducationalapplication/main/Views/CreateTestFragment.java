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
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ItemTextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.OcrDrawingDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.PhotoDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.Adapters.CustomFinalPageAdapter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.CustomFinalViewPager;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.CustomViewPager;
import com.smallredtracktor.yourpersonaleducationalapplication.root.App;


import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;



@SuppressLint("ValidFragment")
public class CreateTestFragment extends Fragment implements
        ICreateTestFragmentMVPprovider.IFragment,
        MainActivity.BackPressedListener{

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PICK_IMAGE = 2;
    public static final String STUB_PARAM_ID = "NEW";
    public static final String OUTCOME_PARAM_COUNT = "outcome_param_count";
    static final String OUTCOME_PARAM_TICKET_ID = "outcome_param_ticket_id";
    public static final String STUB_PARAM = "CLICK TO ADD ANSWER";
    private static final String APP_ITEM_TYPE = "type";
    private static final String APP_ITEM_IS_QUESTION = "isQuestion";
    private static final String FILE_PATH = "file_path";
    private static final int CODE_REQUEST_CAMERA = 200;


    private Bundle options;
    private String outcomeParam;
    private String ticketId;


    @BindView(R.id.ticketCounterTextView)
    TextView ticketCounterTextView;
    @BindView(R.id.questionTextView)
    TextView questionTextView;
    @BindView(R.id.answerImageView)
    SubsamplingScaleImageView questionImageView;
    @BindView(R.id.viewPagerLayout)
    FrameLayout viewPagerLayout;
    @BindView(R.id.smallViewPager)
    CustomFinalViewPager smallViewPager;
    @BindView(R.id.createTestRootConstraintLayout)
    ConstraintLayout createTestRootConstraintLayout;

    @Inject
    ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter;
    @Inject
    ChooseSourceDialog chooseSourceDialog;
    @Inject
    PhotoDialog photoDialog;
    @Inject
    ItemTextDialog textDialog;
    @Inject
    OcrDrawingDialog ocrDrawingDialog;

    private CustomFinalPageAdapter smallAdapter;
    private Unbinder unbinder;
    private ViewGroup.LayoutParams layoutParams;

    public CreateTestFragment() {
    }

    public static CreateTestFragment newInstance(String outcome, String id) {
        CreateTestFragment fragment = new CreateTestFragment();
        Bundle args = new Bundle();
        args.putString(OUTCOME_PARAM_COUNT, outcome);
        args.putString(OUTCOME_PARAM_TICKET_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            outcomeParam = getArguments().getString(OUTCOME_PARAM_COUNT);
            ticketId = getArguments().getString(OUTCOME_PARAM_TICKET_ID);
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
        layoutParams = viewPagerLayout.getLayoutParams();
        ((MainActivity) Objects.requireNonNull(getActivity())).addBackPressedListener(this);
        questionImageView.setZoomEnabled(false);
        questionImageView.setOnClickListener(v -> createTestFragmentPresenter.onAddQuestionClick());
        questionImageView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(STUB_PARAM_ID));
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smallAdapter = new CustomFinalPageAdapter(getChildFragmentManager());
        smallAdapter.setViewPager(smallViewPager);
        smallAdapter.setPresenter(createTestFragmentPresenter);
        smallViewPager.setAdapter(smallAdapter);
        smallAdapter.addFirstItem();
        smallViewPager.setCurrentItem(1);
        smallViewPager.setOffscreenPageLimit(50);
        smallViewPager.setCurrentItem(1);
        smallViewPager.setPageMargin(200);
        smallViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int var1, float var2, int var3) {}
            @Override
            public void onPageSelected(int page)
            {
                createTestFragmentPresenter.onAnswerPageSelected(page, smallAdapter.getCount());
            }
            @Override
            public void onPageScrollStateChanged(int var1) {
            }});
    }

    @Override
    public void onResume() {
        createTestFragmentPresenter.setView(this);
        super.onResume();
        createTestFragmentPresenter.onViewResumed(outcomeParam, ticketId);
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
        Bitmap white = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
        white.eraseColor(getResources().getColor(R.color.colorBack));
        questionImageView.setImage(ImageSource.cachedBitmap(white));
        questionImageView.setOnClickListener(v -> createTestFragmentPresenter.onAddQuestionClick());
        questionImageView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(STUB_PARAM_ID));
    }

    @Override
    public void setTextQuestion(String id, int type, String content) {
        questionTextView.setText(content);
        Bitmap white = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
        white.eraseColor(getResources().getColor(R.color.colorBack));
        questionImageView.setImage(ImageSource.cachedBitmap(white));
        questionImageView.setOnClickListener(v -> createTestFragmentPresenter.onQuestionPressed(id));
        questionImageView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(id));
    }

    @Override
    public void setPhotoQuestion(String id, int type, Bitmap content) {
        questionImageView.setImage(ImageSource.cachedBitmap(content));
        questionTextView.setText("");
        questionImageView.setOnClickListener(v -> createTestFragmentPresenter.onQuestionPressed(id));
        questionImageView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(id));
    }

    @Override
    public void setCurrentAnswer(String id, int type, String content) {
        smallAdapter.setItem(id, type, content);
    }


    @Override
    public void addNewAnswer() {
        smallAdapter.addItem(STUB_PARAM_ID, STUB_PARAM);

    }

    @Override
    public void removeAnswer(String id) {
        smallAdapter.removeItem(id);
    }

    @Override
    public void showPhotoFragment(String id, String value, int type, boolean isQuestion) {
        photoDialog.setDialogParams(id, value, type, isQuestion);
        photoDialog.show();
    }

    @Override
    public void showTextFragment(String id, String text, int type, boolean isQuestion) {
        textDialog.setDialogParams(text, id, type, isQuestion);
        textDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showOcrDrawingDialog(String id, String path) {
        ocrDrawingDialog.setDialogParams(id, path);
        ocrDrawingDialog.show();
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
    public void showChooseSourceDialog(boolean isQuestion) {
        chooseSourceDialog.setIsQuestion(isQuestion);
        chooseSourceDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void resolveCameraPermission() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CODE_REQUEST_CAMERA);
        }
    }

    @Override
    public void setCurrentAnswerItem(int position) {
        smallViewPager.setCurrentItem(position);
    }

    @Override
    public void switchPagerToFullScreen() {
        Objects.requireNonNull(((AppCompatActivity)
                Objects.requireNonNull(
                        getActivity())).getSupportActionBar()).hide();
        viewPagerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        viewPagerLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        createTestFragmentPresenter.onViewModeChanged(true);
    }

    @Override
    public void switchPagerToSmallView() {
        Objects.requireNonNull(((AppCompatActivity)
                Objects.requireNonNull
                        (getActivity())).getSupportActionBar()).show();
        viewPagerLayout.setBackgroundColor(getResources().getColor(R.color.colorBack));
        viewPagerLayout.setLayoutParams(layoutParams);
        createTestFragmentPresenter.onViewModeChanged(false);
    }


    @Override
    public void animateAnswer(String id, MotionEvent e2) {
        ((AnswerContentFragment)smallAdapter.getItemById(id)).animate(e2);
    }

    @Override
    public void calculateAnswerScroll(String id, MotionEvent e) {
        ((AnswerContentFragment)smallAdapter.getItemById(id)).calculateScroll(e);
    }

    @Override
    public void scrollAnswer(String id, MotionEvent event) {
        ((AnswerContentFragment)smallAdapter.getItemById(id)).slideView(event);
    }

    @Override
    public void notifyAdapterViewModeChanged(boolean isFullScreenMode) {
        smallAdapter.setViewMode(isFullScreenMode);
    }

    @Override
    public void resetAnswerTransition(String id) {
        ((AnswerContentFragment)smallAdapter.getItemById(id)).resetTransition();
    }

    @Override
    public void onBackPressed() {
        createTestFragmentPresenter.onBackPressed();
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
