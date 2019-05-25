package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ItemTextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.OcrDrawingDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CompressUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.GalleryPathUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.PhotoIntentUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableObserver;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.STUB_PARAM_ID;

public class CreateTestFragmentPresenter implements
        ICreateTestFragmentMVPprovider.IPresenter,
        ChooseSourceDialog.ChooseSourceDialogListener,
        ItemTextDialog.TextDialogListener,
        OcrDrawingDialog.OcrDrawingDialogListener {

    public static final String STATUS_LOADING = "loading";
    private static final String MESSAGE_NETWORK_ERROR = "error while networking";

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
    private HashMap<String, DisposableMaybeObserver<OcrResponseModel>> readOcrSubscriberMap = new HashMap<>();
    private String currentTicket;
    private boolean isFullScreenMode = false;

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
                        s -> Objects.requireNonNull(compressUtil)
                                .getBitmap(s))
                        .doOnSuccess(bitmap ->
                                view.setPhotoQuestion(id, type, bitmap))
                        .subscribe();
            }
            else
                {
                    o.doOnSuccess(s -> {
                        view.setCurrentAnswer(id, type, s);
                        view.addNewAnswer();
                    }).subscribe();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (view != null) {
            view.switchPagerToSmallView();
        }
    }



    @Override
    public void onPhotoTaken(String path, int type, boolean isQuestion) {
        if (view != null && model != null) {
            String id = UniqueDigit.getUnique();
            model.updateTestItem(id, isQuestion, currentTicket, 1, path);
                        if (isQuestion) {
                            if(type == 1) {
                                Objects.requireNonNull(compressUtil)
                                    .getBitmap(path)
                                    .doOnSuccess(bitmap ->
                                            view.setPhotoQuestion(id, 1, bitmap))
                                        .subscribe();
                            } else if (type == 3) {
                                view.setTextQuestion(id, type, STATUS_LOADING);
                                registerOcrDataQuestionConsumer(id, path);
                            }
                        } else {
                            if(type == 1) {
                                view.setCurrentAnswer(id, type, path);
                            } else if (type == 3) {
                                view.setCurrentAnswer(id, 3, null);
                                view.showOcrDrawingDialog(id, path);
                                //registerOcrDataAnswerConsumer(id, path);
                            }
                            view.addNewAnswer();
                        }
        }
    }

    private void registerOcrDataAnswerConsumer(String id, String path) {
        if(model != null && view != null) {
            readOcrSubscriberMap.put(id, new DisposableMaybeObserver<OcrResponseModel>() {
                        @Override
                        public void onSuccess(OcrResponseModel ocrResponseModel) {
                            try
                            {
                                String text = ocrResponseModel.getText();
                                model.updateTestItem(id, false, currentTicket, 3, text);
                                model.deleteFile(path);
                                view.setCurrentAnswer(id, 3, text);
                            }catch (Exception e)
                            {
                                view.showToast(MESSAGE_NETWORK_ERROR);
                                view.setCurrentAnswer(id, 1, path);
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            view.setCurrentAnswer(id, 1, path);
                        }
                        @Override
                        public void onComplete() {}});
            ocrData(path)
                    .subscribe(Objects.requireNonNull(readOcrSubscriberMap.get(id)));
        }
    }

    private void registerOcrDataQuestionConsumer(String id, String path) {
        if(model != null && view != null)
        {
            readOcrSubscriberMap.put(id, new DisposableMaybeObserver<OcrResponseModel>() {
                @Override
                public void onSuccess(OcrResponseModel ocrResponseModel) {
                    String text = ocrResponseModel.getText();
                    model.updateTestItem(id, true, currentTicket, 3, text);
                    model.deleteFile(path);
                    view.setTextQuestion(id, 3, text);
                }

                @Override
                public void onError(Throwable e) {
                    view.showToast(MESSAGE_NETWORK_ERROR);
                    Objects.requireNonNull(compressUtil)
                            .getBitmap(path)
                            .doOnSuccess(bitmap -> view.setPhotoQuestion(id, 1, bitmap))
                            .subscribe();
                }
                @Override
                public void onComplete() {}});
            ocrData(path)
                    .subscribe(Objects.requireNonNull(readOcrSubscriberMap.get(id)));
        }
    }

    private Maybe<OcrResponseModel> ocrData(String path) {
        Maybe<OcrResponseModel> ocrResponse = null;
        if (model != null)
        {
            ocrResponse = model
                    .getParsedTextResult(path);
        }
        return ocrResponse;
    }


    @Override
    public void onViewResumed(String s, String ticketId) {
        if (view != null) {
            view.setCounterTextView(s);
            currentTicket = ticketId;
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
    public void onOcrDrawingDialogUndo(String id, Bitmap takenImage) {

    }

    @Override
    public void onOcrDrawingDialogRedo(String id, Bitmap selectedBitmapRegion) {

    }

    @Override
    public void onOcrDrawingDialogEdit(String id, Object o) {

    }

    @Override
    public void onOcrDrawingDialogTouchModeFabOn(String id, Object o) {

    }

    @Override
    public void onOcrDrawingDialogDone(String id, Object o) {

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

    @Override
    public void onAnswerPageSelected(int currentPage, int count) {
        if(view != null) {
            if (currentPage == 0) {
                if (isFullScreenMode) {
                    view.switchPagerToSmallView();
                }
                view.setCurrentAnswerItem(1);
            }
            if (currentPage == count - 1) {
                if (isFullScreenMode) {
                    view.switchPagerToSmallView();
                }
                view.setCurrentAnswerItem(count - 2);
            }
        }
    }

    @Override
    public void onAnswerFragmentClick(String id) {
        if (view != null && model != null) {
            if (id.equals(STUB_PARAM_ID)) {
                view.showChooseSourceDialog(false);
            } else
            {
                if (!isFullScreenMode) {
                    view.switchPagerToFullScreen();
                }
            }
        }
    }

    @Override
    public void onAnswerViewSwipe(String id) {
        if (view != null && model != null) {
            if(!isFullScreenMode)
            {
                view.removeAnswer(id);
                view.setCurrentAnswerItem(1);
                unsubscribeById(id);
                model.deleteTestItem(id);
            } else
            {
                view.resetAnswerTransition(id);
                view.switchPagerToSmallView();
            }
        }
    }


    @Override
    public void onAnswerDoubleTap(String id) {
        if (view != null) {
            if(isFullScreenMode)
            view.showToast("WOW");
        }
    }

    @Override
    public void onViewModeChanged(boolean isFullScreenMode) {
        this.isFullScreenMode = isFullScreenMode;
        if (view != null) {
            view.notifyAdapterViewModeChanged(isFullScreenMode);
        }
    }

    @Override
    public void onAnswerScroll(String id, MotionEvent e2) {
        if (view != null) {
            view.animateAnswer(id, e2);
        }
    }

    @Override
    public void onAnswerDown(String id, MotionEvent e) {
        if (view != null) {
            view.calculateAnswerScroll(id,e);
        }
    }

    @Override
    public void onAnswerFragmentUp(String id, MotionEvent event) {
        if (view != null) {
            view.scrollAnswer(id, event);
        }
    }



    private void unsubscribeById(String id) {
        if(readSubscriberMap.containsKey(id))
        {
            Objects.requireNonNull(readSubscriberMap.get(id)).dispose();
        }
        if(readOcrSubscriberMap.containsKey(id))
        {
            Objects.requireNonNull(readOcrSubscriberMap.get(id)).dispose();
        }
    }

    @Override
    public void rxUnsubscribe() {
        for (DisposableObserver<List<TestItem>> listDisposableObserver : readSubscriberMap.values()) {
            listDisposableObserver.dispose();
        }
        for (DisposableMaybeObserver<OcrResponseModel> listDisposableMaybeObserver : readOcrSubscriberMap.values()) {
            listDisposableMaybeObserver.dispose();
        }
    }


}

