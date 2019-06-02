package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
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
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.PolygonCropUtil;
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
    @Nullable
    private PolygonCropUtil polygonCropUtil;


    private HashMap<String,DisposableObserver<List<TestItem>>> readStorageSubscriberMap = new HashMap<>();
    private HashMap<String, DisposableMaybeObserver<OcrResponseModel>> readOcrDataSubscriberMap = new HashMap<>();
    private String currentTicket;
    private boolean isFullScreenMode = false;

    public CreateTestFragmentPresenter(@Nullable ICreateTestFragmentMVPprovider.IModel model,
                                       @Nullable CompressUtil compressUtil,
                                       @Nullable GalleryPathUtil galleryPathUtil,
                                       @Nullable PhotoIntentUtil photoIntentUtil,
                                       @Nullable PolygonCropUtil polygonCropUtil) {
        this.model = model;
        this.compressUtil = compressUtil;
        this.galleryPathUtil = galleryPathUtil;
        this.photoIntentUtil = photoIntentUtil;
        this.polygonCropUtil = polygonCropUtil;
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
            readStorageSubscriberMap.put(id ,new DisposableObserver<List<TestItem>>() {
                @Override
                public void onNext(List<TestItem> testItems) {
                    if (testItems.size() != 0) {
                        try {
                            TestItem item = testItems.get(0);
                            if(item.getType() == 0 || item.getType() ==3)
                            {
                                view.showTextFragment(item.getId(),
                                        item.getValue(),
                                        item.getType() ,
                                        item.isQuestion());
                            } else if(item.getType() == 1 || item.getType() == 2)
                            {
                                String path = item.getValue();
                                if (compressUtil != null) {
                                    compressUtil
                                            .getFullSizeBitmap(path)
                                            .doOnSuccess(bitmap ->
                                                    view.showPhotoFragment(item.getId(),
                                                    bitmap, item.getType() , item.isQuestion()))
                                            .subscribe();
                                }
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
            readTestItem.subscribe(Objects.requireNonNull(readStorageSubscriberMap.get(id)));
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
                                .getBitmapSample(s))
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
            if(isFullScreenMode)
            {
                view.switchPagerToSmallView();
            }
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
                                    .getBitmapSample(path)
                                    .doOnSuccess(bitmap ->
                                            view.setPhotoQuestion(id, 1, bitmap))
                                        .subscribe();
                            } else if (type == 3) {
                                Objects.requireNonNull(compressUtil)
                                        .getFullSizeBitmap(path)
                                        .doOnSuccess(bitmap ->
                                                view.showOcrDrawingDialog(id, bitmap, path, true))
                                        .subscribe();
                            }
                        } else {
                            if(type == 1) {
                                view.setCurrentAnswer(id, type, path);
                            } else if (type == 3) {
                                Objects.requireNonNull(compressUtil)
                                        .getFullSizeBitmap(path)
                                        .doOnSuccess(bitmap ->
                                                view.showOcrDrawingDialog(id, bitmap, path, false))
                                        .subscribe();
                            }
                            view.addNewAnswer();
                        }
        }
    }

    private void registerOcrDataAnswerConsumer(String id, String path, Bitmap bitmap) {
        if(model != null && view != null) {
            readOcrDataSubscriberMap.put(id, new DisposableMaybeObserver<OcrResponseModel>() {
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
            ocrData(bitmap)
                    .subscribe(Objects.requireNonNull(readOcrDataSubscriberMap.get(id)));
        }
    }

    private Maybe<OcrResponseModel> ocrData(Bitmap bitmap) {
            Maybe<OcrResponseModel> ocrResponse = null;
            if (model != null)
            {
                ocrResponse = model
                        .getParsedTextResult(bitmap);
            }
            return ocrResponse;
    }

    private void registerOcrDataQuestionConsumer(String id, String path, Bitmap bitmap) {
        if(model != null && view != null)
        {
            readOcrDataSubscriberMap.put(id, new DisposableMaybeObserver<OcrResponseModel>() {
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
                            .getBitmapSample(path)
                            .doOnSuccess(bitmap -> view.setPhotoQuestion(id, 1, bitmap))
                            .subscribe();
                }
                @Override
                public void onComplete() {}});
            ocrData(bitmap)
                    .subscribe(Objects.requireNonNull(readOcrDataSubscriberMap.get(id)));
        }
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
                .getPhotoTakingSet()
                .doOnSuccess(result ->
                        view.showCameraFragment(
                                result.getIntent(),
                                type,
                                isQuestion,
                                result.getPath()))
                .subscribe();
        }
    }

    @Override
    public void onDialogGallerySourceClick(boolean isQuestion) {
        if (view != null) {
            if (galleryPathUtil != null) {
                Intent intent = galleryPathUtil.getPreparedPhotoPickerIntent();
                view.showGallery(2, isQuestion, intent);
            }
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
    public void onOcrDrawingDialogUndo() {
        if (view != null) {
            view.undoDrawingDialogViewPoint();
        }
    }

    @Override
    public void onOcrDrawingDialogRedo() {
        if (view != null) {
            view.redoDrawingDialogViewPoint();
        }
    }

    @Override
    public void onOcrDrawingDialogEdit() {
        if (view != null) {
            view.setOcrDrawingDialogMode(true);
        }
    }

    @Override
    public void onOcrDrawingDialogTouchModeFabOn() {
        if (view != null) {
            view.setOcrDrawingDialogMode(false);
        }
    }

    @Override
    public void onOcrDrawingDialogDone(Bitmap src, List<PointF> polygons, Bitmap mutable, String id, String path, boolean isQuestion) {
        if (view != null) {
            view.closeOcrDrawingDialog();
        }
        if (polygonCropUtil != null) {
            polygonCropUtil.getCroppedBitmap(src, polygons, mutable)
                    .doOnSuccess(bitmap ->
                    {
                        if(isQuestion)
                        {
                            view.setTextQuestion(id, 3, STATUS_LOADING);
                            registerOcrDataQuestionConsumer(id,path, bitmap);
                        } else
                            {
                                view.setCurrentAnswer(id, 3, null);
                                registerOcrDataAnswerConsumer(id, path, bitmap);
                            }

                    })
                    .subscribe();
        }
    }

    @Override
    public void onOcrDrawingDialogPolygonClosed() {

    }

    @Override
    public void onDrawerViewActionDown(MotionEvent event) {
        if (view != null) {
            if (event.getActionIndex() == 0)
            {
                view.addPointToDrawerView(event.getX(), event.getY());
            }
        }
    }

    @Override
    public void onDrawerViewActionMove(MotionEvent event) {
        if (view != null) {
            int touchCount = event.getPointerCount();
            if(touchCount == 1)
            {
                view.recomputeLastDrawerViewPoint(event.getX(), event.getY());
            }
        }
    }

    @Override
    public void onDrawerViewActionUp(MotionEvent event) {
        if (view != null) {
            view.addPointToDrawerView(event.getX(), event.getY());
        }
    }


    @Override
    public void onQuestionLongPressed(String id) {
        if (view != null && model != null) {
            if(!id.equals(STUB_PARAM_ID))
            {
                view.deleteQuestion();
                unsubscribeById(id);
                model.deleteTestItem(id);
            }
        }
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
            view.animateAnswer(id, e2.getRawY());
        }
    }

    @Override
    public void onAnswerDown(String id, MotionEvent e) {
        if (view != null) {
            view.calculateAnswerScroll(id, e.getRawY());
        }
    }

    @Override
    public void onAnswerFragmentUp(String id, MotionEvent event) {
        if (view != null) {
            view.scrollAnswer(id, event.getRawY());
        }
    }


    private void unsubscribeById(String id) {
        if(readStorageSubscriberMap.containsKey(id))
        {
            Objects.requireNonNull(readStorageSubscriberMap.get(id)).dispose();
        }
        if(readOcrDataSubscriberMap.containsKey(id))
        {
            Objects.requireNonNull(readOcrDataSubscriberMap.get(id)).dispose();
        }
    }

    private void rxUnsubscribe() {
        for (DisposableObserver<List<TestItem>> listDisposableObserver : readStorageSubscriberMap.values()) {
            listDisposableObserver.dispose();
        }
        for (DisposableMaybeObserver<OcrResponseModel> listDisposableMaybeObserver : readOcrDataSubscriberMap.values()) {
            listDisposableMaybeObserver.dispose();
        }
    }

    @Override
    public void onViewDestroyed() {
        rxUnsubscribe();
    }
}

