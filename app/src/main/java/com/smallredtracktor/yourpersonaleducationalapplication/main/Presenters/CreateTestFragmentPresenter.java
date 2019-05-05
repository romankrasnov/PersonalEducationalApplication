package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.TextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CompressUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.GalleryPathUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.PhotoIntentUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.STUB_PARAM_ID;

public class CreateTestFragmentPresenter implements
        ICreateTestFragmentMVPprovider.IPresenter,
        ChooseSourceDialog.ChooseSourceDialogListener,
        TextDialog.TextDialogListener {

    public static final String STATUS_LOADING = "loading";

    @Nullable
    private GalleryPathUtil galleryPathUtil;
    @Nullable
    private ICreateTestFragmentMVPprovider.IFragment view;
    @Nullable
    private ICreateTestFragmentMVPprovider.IModel model;
    @Nullable
    private CompressUtil compressUtil;
    @Nullable
    private PhotoIntentUtil photoIntentUtil;

    private HashMap<String,DisposableObserver<List<TestItem>>> readSubscriberMap = new HashMap<>();
    private int currentTicket;



    public CreateTestFragmentPresenter(@Nullable ICreateTestFragmentMVPprovider.IModel model,
                                       @Nullable CompressUtil compressUtil,
                                       @Nullable GalleryPathUtil galleryPathUtil,
                                       @Nullable PhotoIntentUtil photoIntentUtil) {
        this.model = model;
        this.compressUtil = compressUtil;
        this.galleryPathUtil = galleryPathUtil;
        this.photoIntentUtil = photoIntentUtil;
    }


    @Override
    public void setView(@Nullable ICreateTestFragmentMVPprovider.IFragment view) {
        this.view = view;
    }

    @Override
    public void onAddQuestionClick() {
        if (view != null) {
            view.showChooseSourceDialog(true);
        }
    }

    @Override
    public void onQuestionPressed(String id) {
        if (view != null && model != null) {
            Observable<List<TestItem>> readTestItem = model
                    .getTestItem(id)
                    .toObservable();
            readSubscriberMap.put(id ,new DisposableObserver<List<TestItem>>() {
                @Override
                public void onNext(List<TestItem> testItems) {
                    if (testItems.size() != 0) {
                        try {
                            if(testItems.get(0).getType() == 0 || testItems.get(0).getType() ==3)
                            {
                                view.showTextFragment(testItems.get(0).getId(),
                                        testItems.get(0).getValue(),
                                        testItems.get(0).getType() ,
                                        testItems.get(0).isQuestion());
                            } else if(testItems.get(0).getType() == 1 || testItems.get(0).getType() == 2)
                            {
                                view.showPhotoFragment(testItems.get(0).getId(),
                                        testItems.get(0).getValue(),
                                        testItems.get(0).getType() ,
                                        testItems.get(0).isQuestion());
                            }
                        } catch (Exception e) {
                            view.showToast(testItems.get(0).getValue());
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    view.showToast(e.getMessage());
                }

                @Override
                public void onComplete() {
                }
            });
            readTestItem.subscribe(Objects.requireNonNull(readSubscriberMap.get(id)));
        }
    }


    @Override
    public void onGalleryResult(Uri path, int type, boolean isQuestion) {
        if (view != null && model != null) {
            String id = UniqueDigit.getUnique();

            Single<String> o = Objects.requireNonNull(galleryPathUtil)
                    .save(path)
                    .doOnSuccess(s -> model.updateTestItem(id, isQuestion, currentTicket, type, s));
            if(isQuestion)
            {
                o.flatMap((Function<String, Single<Bitmap>>)
                        s -> Objects.requireNonNull(compressUtil).getBitmap(s))
                        .doOnSuccess(bitmap ->
                                view.setPhotoQuestion(id, type, bitmap)).subscribe();
            }
            else
                {
                    o.doOnSuccess(s -> {
                        view.setCurrentAnswer(id, type, s);
                        view.addNewAnswer(); })
                            .subscribe();
                }
        }
    }

    @Override
    public void onBackPressed() {

    }


    @Override
    public void onAcceptSubject() {

    }


    @Override
    public void onPhotoTaken(String path, int type, boolean isQuestion) {
        if (view != null && model != null) {
            String id = UniqueDigit.getUnique();
            if (isQuestion) {
                if(type == 1)
                {
                    Objects.requireNonNull(compressUtil)
                            .getBitmap(path)
                            .doOnSuccess(bitmap -> view.setPhotoQuestion(id, 1, bitmap))
                            .subscribe();
                    model.updateTestItem(id, true, currentTicket, 1, path);
                } else if (type == 3)
                    {
                        view.setTextQuestion(id, type, STATUS_LOADING);
                        ocrData(path).doOnSuccess(ocrResponseModel -> {
                            String text = ocrResponseModel.getText();
                                    model.updateTestItem(id, true, currentTicket, 3, text);
                                    view.setTextQuestion(id, 3, text);
                        }).subscribe();
                    }
            } else {
                if(type == 1)
                {
                    view.setCurrentAnswer(id, type, path);
                    view.addNewAnswer();
                    model.updateTestItem(id, false, currentTicket, 1, path);
                } else if (type == 3)
                    {
                    view.setCurrentAnswer(id, type, null);
                    view.addNewAnswer();
                    ocrData(path).doOnSuccess(ocrResponseModel -> {
                        String text = ocrResponseModel.getText();
                        model.updateTestItem(id, false, currentTicket, 3, text);
                        view.setCurrentAnswer(id, 3, text);
                    }).subscribe();
                }

            }
        }
    }

    private Single<OcrResponseModel> ocrData(String path) {
        Single<OcrResponseModel> ocrResponse = null;
        if (model != null)
        {
            ocrResponse = model
                    .getParsedTextResult(path);
        }
        return ocrResponse;
    }


    @Override
    public void rxUnsubscribe() {
        for (DisposableObserver<List<TestItem>> listDisposableObserver : readSubscriberMap.values()) {
            listDisposableObserver.dispose();
        }
    }

    @Override
    public void onViewResumed(String s) {
        if (view != null) {
            view.setCounterTextView(s);
            currentTicket = Integer.parseInt(s);
        }
    }



    @Override
    public void onDialogTextSourceClick(boolean isQuestion) {
        if (view != null) {
            view.showTextFragment(UniqueDigit.getUnique(), "", 0, isQuestion);
        }
    }

    @Override
    public void onDialogPhotoSourceClick(boolean isQuestion) {
            fireCamera(1, isQuestion);
    }

    private void fireCamera(int type, boolean isQuestion) {
        if (view != null) {
            view.resolveCameraPermission();
        Objects.requireNonNull(photoIntentUtil)
                .get()
                .doOnSuccess(objects -> view.showCameraFragment((Intent) objects[0],
                        type, isQuestion, (String) objects[1]))
                .subscribe();
        }
    }

    @Override
    public void onDialogGallerySourceClick(boolean isQuestion) {
        if (view != null) {
            view.showGallery(2, isQuestion);
        }
    }

    @Override
    public void onDialogOcrSourceClick(boolean isQuestion) {
        fireCamera(3, isQuestion);
    }

    @Override
    public void onTextDialogCreate(String id) {
        unsubscribeById(id);
    }

    @Override
    public void onTextDialogInteraction(String id, String content, int type, boolean isQuestion) {
        if (model != null && view != null) {
            model.updateTestItem(id, isQuestion, currentTicket, type, content);
            if (isQuestion) {
                view.setTextQuestion(id, type, content);
            } else {
                view.setCurrentAnswer(id, type, content);
                view.addNewAnswer();
            }
        }
    }

    @Override
    public void onAnswerFragmentInteraction(String id) {
        if (view != null && model != null) {
            if (id.equals(STUB_PARAM_ID)) {
                view.showChooseSourceDialog(false);
            } else {
                Observable<List<TestItem>> readTestItem = model
                        .getTestItem(id)
                        .toObservable();
                readSubscriberMap.put(id, new DisposableObserver<List<TestItem>>() {
                    @Override
                    public void onNext(List<TestItem> testItems) {
                        if (testItems.size() != 0) {
                            try {
                                if(testItems.get(0).getType() == 0 || testItems.get(0).getType() == 3)
                                {
                                    view.showTextFragment(testItems.get(0).getId(),
                                            testItems.get(0).getValue(),
                                            testItems.get(0).getType() ,
                                            testItems.get(0).isQuestion());
                                } else if(testItems.get(0).getType() == 1 || testItems.get(0).getType() == 2)
                                {
                                    view.showPhotoFragment(testItems.get(0).getId(),
                                            testItems.get(0).getValue(),
                                            testItems.get(0).getType() ,
                                            testItems.get(0).isQuestion());
                                }
                            } catch (Exception e) {
                                view.showToast(testItems.get(0).getValue());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                readTestItem.subscribe(Objects.requireNonNull(readSubscriberMap.get(id)));
            }
        }
    }

    @Override
    public void onAnswerLongClick(String id) {
        if (view != null && model != null) {
            if(!id.equals(STUB_PARAM_ID))
            {
                view.removeAnswer(id);
                unsubscribeById(id);
                model.deleteTestItem(id);
            }
        }
    }

    @Override
    public boolean onQuestionLongPressed(String id) {
        if (view != null && model != null) {
            if(!id.equals(STUB_PARAM_ID))
            {
                view.deleteQuestion();
                unsubscribeById(id);
                model.deleteTestItem(id);
            }
        }
        return false;
    }

    private void unsubscribeById(String id) {
        if(readSubscriberMap.containsKey(id))
        {
            Objects.requireNonNull(readSubscriberMap.get(id)).dispose();
        }
    }
}

