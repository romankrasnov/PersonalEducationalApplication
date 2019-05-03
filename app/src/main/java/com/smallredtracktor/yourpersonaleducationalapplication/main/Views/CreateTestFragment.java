package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.PhotoDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.TextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.GalleryPathUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.PhotoIntent;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.MyOwnPageAdapter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.MyViewPager;
import com.smallredtracktor.yourpersonaleducationalapplication.root.App;

import java.util.Objects;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;


@SuppressLint("ValidFragment")
public class CreateTestFragment extends Fragment implements
        ICreateTestFragmentMVPprovider.IFragment {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PICK_IMAGE = 2;
    public static final String STUB_PARAM_ID = "new" ;
    private static final String OUTCOME_PARAM = "outcome_param";
    public static final String STUB_PARAM = "CLICK TO ADD ANSWER";
    private static final String APP_ITEM_TYPE = "type";
    private static final String APP_ITEM_IS_QUESTION = "isQuestion";
    private static final String FILE_PATH = "file_path" ;
    private Bundle options;
    private String outcomeParam;

    @BindView(R.id.ticketCounterTextView)
    TextView ticketCounterTextView;
    @BindView(R.id.addQuestionView)
    TextView addQuestionView;
    @BindView(R.id.viewPager)
    MyViewPager viewPager;


    @Inject
    ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter;
    MyOwnPageAdapter adapter;


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
        ButterKnife.bind(this, view);
        App.get(Objects.requireNonNull(getContext()))
                .getComponent()
                .plusCreateTestComponent(new CreateTestModule(getContext()))
                .inject(this);
        addQuestionView.setOnClickListener(v -> createTestFragmentPresenter.onAddQuestionClick());
        addQuestionView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(STUB_PARAM_ID));

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
        addQuestionView.setText("CLICK TO ADD QUESTION");
        addQuestionView.setBackgroundColor(Color.TRANSPARENT);
        addQuestionView.setOnClickListener(v -> createTestFragmentPresenter.onAddQuestionClick());
        addQuestionView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(STUB_PARAM_ID));
    }

    @Override
    public void setTextQuestion(String id, int type, String content) {
        addQuestionView.setText(content);
        addQuestionView.setOnClickListener(v -> createTestFragmentPresenter.onQuestionPressed(id));
        addQuestionView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(id));
    }

    @Override
    public void setPhotoQuestion(String id, int type, String content) {
        addQuestionView.setText("");
        Observable.just(Objects.requireNonNull(Drawable.createFromPath(content)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(drawable -> addQuestionView.setBackgroundDrawable(drawable))
                .doOnComplete(() -> Log.d("drawfrompath",Thread.currentThread().getName() + "drawfrompath"))
                .subscribe();
        addQuestionView.setOnClickListener(v -> createTestFragmentPresenter.onQuestionPressed(id));
        addQuestionView.setOnLongClickListener(v -> createTestFragmentPresenter.onQuestionLongPressed(id));
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
        PhotoDialog dialog = new PhotoDialog(getContext(), value, type, isQuestion);
        dialog.show();
    }

    @Override
    public void showTextFragment(String id, String text, int type, boolean isQuestion) {
        TextDialog dialog = new TextDialog(createTestFragmentPresenter);
        dialog.setDialogParams(text, id, type, isQuestion);
        dialog.show(getChildFragmentManager(), null);
    }


    @Override
    public void showCameraFragment(int type, boolean isQuestion) {
        Intent takePictureIntent = PhotoIntent.newInstance(Objects.requireNonNull(getContext()));
        options = new Bundle();
        options.putInt(APP_ITEM_TYPE, type);
        options.putBoolean(APP_ITEM_IS_QUESTION, isQuestion);
        options.putString(FILE_PATH, PhotoIntent.getPath());
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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
        DialogFragment dialog = new ChooseSourceDialog(createTestFragmentPresenter, isQuestion);
        dialog.show(getChildFragmentManager(), null);
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
            String result = GalleryPathUtil.save(contentURI, Objects.requireNonNull(getActivity()));
             createTestFragmentPresenter.onGalleryResult(result,
                            options.getInt(APP_ITEM_TYPE),
                            options.getBoolean(APP_ITEM_IS_QUESTION));
        }
    }
}
