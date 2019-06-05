package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
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
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.PageTransformers.CardStackPageTransformer;
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
        MainActivity.BackPressedListener {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PICK_IMAGE = 2;
    public static final String STUB_PARAM_ID = "NEW";
    public static final String OUTCOME_PARAM_COUNT = "outcome_param_count";
    static final String OUTCOME_PARAM_TICKET_ID = "outcome_param_ticket_id";
    public static final String STUB_PARAM_ANSWER = "CLICK TO ADD ANSWER";
    private static final String STUB_PARAM_QUESTION = "CLICK TO ADD QUESTION";
    private static final String APP_ITEM_TYPE = "type";
    private static final String APP_ITEM_IS_QUESTION = "isQuestion";
    private static final String FILE_PATH = "file_path";
    private static final int CODE_REQUEST_CAMERA = 200;


    private Bundle imagePickingIntentOptions;
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
    @BindView(R.id.mainCard)
    CardView mainCard;

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
    private ActionBar appBar;
    private CardStackPageTransformer cardPageTransformer;
    private Bitmap whiteQuestionBitmap;

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
        cardPageTransformer = new CardStackPageTransformer();
        layoutParams = viewPagerLayout.getLayoutParams();
        appBar = Objects.requireNonNull(((AppCompatActivity)
                Objects.requireNonNull(
                        getActivity())).getSupportActionBar());
        ((MainActivity) Objects.requireNonNull(getActivity())).addBackPressedListener(this);
        questionImageView.setZoomEnabled(false);
        whiteQuestionBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
        whiteQuestionBitmap.eraseColor(getResources().getColor(R.color.colorBack));
        questionImageView.setOnClickListener(v -> createTestFragmentPresenter.onAddQuestionClick());
        questionImageView.setOnLongClickListener(v -> {
            createTestFragmentPresenter.onQuestionLongPressed(STUB_PARAM_ID);
            return false;
        });
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
        cardPageTransformer.setMode(true);
        smallViewPager.setPageTransformer(true, cardPageTransformer);
        smallViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int var1, float var2, int var3) {
            }
            @Override
            public void onPageSelected(int page) {
                createTestFragmentPresenter.onAnswerPageSelected(page, smallAdapter.getCount());
            }
            @Override
            public void onPageScrollStateChanged(int var1) {
            }
        });
    }

    @Override
    public void onResume() {
        createTestFragmentPresenter.setView(this);
        super.onResume();
        createTestFragmentPresenter.onViewResumed(outcomeParam, ticketId);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        createTestFragmentPresenter = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        createTestFragmentPresenter.onViewDestroyed();
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
        questionTextView.setText(STUB_PARAM_QUESTION);
        questionImageView.setImage(ImageSource.cachedBitmap(whiteQuestionBitmap));
        questionImageView.setOnClickListener(v -> createTestFragmentPresenter.onAddQuestionClick());
        questionImageView.setOnLongClickListener(v -> {
            createTestFragmentPresenter.onQuestionLongPressed(STUB_PARAM_ID);
            return false;
        });
    }



    @Override
    public void setTextQuestion(String id, int type, String content) {
        questionTextView.setText(content);
        questionImageView.setImage(ImageSource.cachedBitmap(whiteQuestionBitmap));
        questionImageView.setOnClickListener(v -> createTestFragmentPresenter.onQuestionPressed(id));
        questionImageView.setOnLongClickListener(v -> {
            createTestFragmentPresenter.onQuestionLongPressed(id);
            return false;
        });
    }

    @Override
    public void setPhotoQuestion(String id, int type, Bitmap content) {
        questionImageView.setImage(ImageSource.cachedBitmap(content));
        questionTextView.setText("");
        questionImageView.setOnClickListener(v -> createTestFragmentPresenter.onQuestionPressed(id));
        questionImageView.setOnLongClickListener(v -> {
            createTestFragmentPresenter.onQuestionLongPressed(id);
            return false;
        });
    }

    @Override
    public void setCurrentAnswer(String id, int type, String content) {
        smallAdapter.setItem(id, type, content);
    }


    @Override
    public void addNewAnswer() {
        smallAdapter.addItem(STUB_PARAM_ID, STUB_PARAM_ANSWER);

    }

    @Override
    public void removeAnswer(String id) {
        smallAdapter.removeItem(id);
    }

    @Override
    public void showPhotoFragment(String id, Bitmap bitmap, int type, boolean isQuestion) {
        photoDialog.setDialogParams(id, bitmap, type, isQuestion);
        photoDialog.show();
    }

    @Override
    public void showTextFragment(String id, String text, int type, boolean isQuestion) {
        textDialog.setDialogParams(text, id, type, isQuestion);
        textDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showOcrDrawingDialog(String id, Bitmap fullBitmap, String path, boolean isQuestion) {
        ocrDrawingDialog.setDialogParams(id, fullBitmap, path, isQuestion);
        ocrDrawingDialog.show();
    }

    @Override
    public void setOcrDrawingDialogMode(boolean mode) {
        ocrDrawingDialog.setDrawingMode(mode);
    }

    @Override
    public void addPointToDrawerView(float x, float y) {
        ocrDrawingDialog.addPointToDrawerView(x,y);
    }

    @Override
    public void recomputeLastDrawerViewPoint(float x, float y) {
        ocrDrawingDialog.recomputeLastDrawerViewPoint(x,y);
    }


    @Override
    public void switchTextItemSwipeMode(String id) {
        ((AnswerContentFragment) smallAdapter.getItemById(id)).switchTextItemCardColor();
    }

    @Override
    public void undoDrawingDialogViewPoint() {
        ocrDrawingDialog.undoDrawingDialogViewPoint();
    }

    @Override
    public void redoDrawingDialogViewPoint() {
        ocrDrawingDialog.redoDrawingDialogViewPoint();
    }

    @Override
    public void closeOcrDrawingDialog() {
        ocrDrawingDialog.dismiss();
    }


    @Override
    public void showCameraFragment(Intent intent, int type, boolean isQuestion, String path) {
        imagePickingIntentOptions = new Bundle();
        imagePickingIntentOptions.putInt(APP_ITEM_TYPE, type);
        imagePickingIntentOptions.putBoolean(APP_ITEM_IS_QUESTION, isQuestion);
        imagePickingIntentOptions.putString(FILE_PATH, path);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    @Override
    public void showGallery(int type, boolean isQuestion, Intent intent) {
        imagePickingIntentOptions = new Bundle();
        imagePickingIntentOptions.putInt(APP_ITEM_TYPE, type);
        imagePickingIntentOptions.putBoolean(APP_ITEM_IS_QUESTION, isQuestion);
        startActivityForResult(intent, PICK_IMAGE);
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
        viewPagerLayout.post(() ->
        {
            appBar.hide();
            mainCard.setCardBackgroundColor(getResources().getColor(R.color.colorBack));
            mainCard.setCardElevation(0);
            mainCard.setUseCompatPadding(false);
            ticketCounterTextView.setVisibility(View.INVISIBLE);
            cardPageTransformer.setMode(false);
            createTestFragmentPresenter.onViewModeChanged(true);
            viewPagerLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

        });
    }

    @Override
    public void switchPagerToSmallView() {
        viewPagerLayout.post(() -> {
            appBar.show();
            mainCard.setCardBackgroundColor(getResources().getColor(R.color.color_card_light));
            mainCard.setCardElevation(2);
            mainCard.setUseCompatPadding(true);
            ticketCounterTextView.setVisibility(View.VISIBLE);
            cardPageTransformer.setMode(true);
            createTestFragmentPresenter.onViewModeChanged(false);
            viewPagerLayout.setLayoutParams(layoutParams);
        });
    }


    @Override
    public void animateAnswer(String id, float rawY) {
        ((AnswerContentFragment) smallAdapter.getItemById(id)).animate(rawY);
    }

    @Override
    public void calculateAnswerScroll(String id, float rawY) {
        ((AnswerContentFragment) smallAdapter.getItemById(id)).calculateScroll(rawY);
    }

    @Override
    public void scrollAnswer(String id, float rawY) {
        ((AnswerContentFragment) smallAdapter.getItemById(id)).slideView(rawY);
    }

    @Override
    public void notifyAdapterViewModeChanged(boolean isFullScreenMode) {
        smallAdapter.setViewMode(isFullScreenMode);
    }

    @Override
    public void resetAnswerTransition(String id) {
        ((AnswerContentFragment) smallAdapter.getItemById(id)).resetTransition();
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
            createTestFragmentPresenter.onPhotoTaken(imagePickingIntentOptions.getString(FILE_PATH),
                    imagePickingIntentOptions.getInt(APP_ITEM_TYPE),
                    imagePickingIntentOptions.getBoolean(APP_ITEM_IS_QUESTION));
        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri contentURI = data.getData();
            createTestFragmentPresenter.onGalleryResult(contentURI,
                    imagePickingIntentOptions.getInt(APP_ITEM_TYPE),
                    imagePickingIntentOptions.getBoolean(APP_ITEM_IS_QUESTION));
        }
    }
}
