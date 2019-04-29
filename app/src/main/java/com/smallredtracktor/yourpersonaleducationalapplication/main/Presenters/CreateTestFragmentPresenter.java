package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import android.support.annotation.Nullable;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.TextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;


import java.util.List;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;



import static android.app.Activity.RESULT_OK;

public class CreateTestFragmentPresenter implements
        ICreateTestFragmentMVPprovider.IPresenter,
        ChooseSourceDialog.ChooseSourceDialogListener,
        TextDialog.TextDialogListener {

    @Nullable
    private ICreateTestFragmentMVPprovider.IFragment view;
    @Nullable
    private ICreateTestFragmentMVPprovider.IModel model;

    private DisposableObserver<List<TestItem>> readPhotoTakingSubscriber;
    private DisposableObserver<List<TestItem>> readQuestionTextTypingSubscriber;
    private DisposableObserver<List<TestItem>> readAnswerTextTypingSubscriber;
    private int currentTicket;


    public CreateTestFragmentPresenter(@Nullable ICreateTestFragmentMVPprovider.IModel model) {
        this.model = model;
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
            readQuestionTextTypingSubscriber = new DisposableObserver<List<TestItem>>() {
                @Override
                public void onNext(List<TestItem> testItems) {
                    if (testItems.size() != 0) {
                        try {
                            view.showTextFragment(testItems.get(0).getValue(), 0, true);
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
            };
            readTestItem.subscribe(readQuestionTextTypingSubscriber);
        }
    }


    @Override
    public void onGalleryResult() {

    }

    @Override
    public void onBackPressed() {

    }


    @Override
    public void onAcceptSubject() {

    }


    @Override
    public void onPhotoTaken(String mPath, int type, boolean isQuestion) {
        if (view != null && model != null) {
            String id = UniqueDigit.getUnique();
            if (isQuestion) {
                view.addQuestion(id);
                    view.setQuestion("loading");
            } else {
                view.setCurrentAnswer(id, type, null);
                    view.addNewAnswer();
            }
            readPhotoTakingSubscriber = new DisposableObserver<List<TestItem>>() {
                @Override
                public void onNext(List<TestItem> testItems) {
                    if (testItems.size() != 0) {
                        try {
                            if (isQuestion) {
                                view.setQuestion(testItems.get(0).getValue());
                            } else {
                                view.setCurrentAnswer(id, type, testItems.get(0).getValue());
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
            };
            Observable<List<TestItem>> readTestItem = null;
            switch (type) {
                case 3: {
                    // noinspection unchecked
                    readTestItem = model
                            .getParsedTextResult(mPath)
                            .flatMap((Function<OcrResponseModel, ObservableSource<List<TestItem>>>) ocrResponseModel -> {
                                model.writeTestItem(id, isQuestion, currentTicket, type, ocrResponseModel.getText());
                                return model.getTestItem(id).toObservable();
                            });
                    break;
                }
                case 1: {
                    // noinspection unchecked
                    model.writeTestItem(id, isQuestion, currentTicket, type, mPath);
                    readTestItem = model
                            .getTestItem(id)
                            .toObservable();
                    break;
                }
            }

            assert readTestItem != null;
            readTestItem
                    .subscribe(readPhotoTakingSubscriber);
        }
    }

    @Override
    public boolean onPhotoPermissionCompatResult(int reqCode, int resCode) {
        switch (reqCode) {
            case 200:
                if (resCode == RESULT_OK) {
                    return true;
                }
        }
        return false;
    }

    @Override
    public void rxUnsubscribe() {
        if (readPhotoTakingSubscriber != null) {
            if (!readPhotoTakingSubscriber.isDisposed()) {
                readPhotoTakingSubscriber.dispose();
            }
        }
        if (readQuestionTextTypingSubscriber != null) {
            if (!readQuestionTextTypingSubscriber.isDisposed()) {
                readQuestionTextTypingSubscriber.dispose();
            }
        }
        if (readAnswerTextTypingSubscriber != null) {
            if (!readAnswerTextTypingSubscriber.isDisposed()) {
                readAnswerTextTypingSubscriber.dispose();
            }
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
    public void onPhotoTakingCancelled() {

    }

    @Override
    public void onDialogTextSourceClick(boolean isQuestion) {
        if (view != null) {
            view.showTextFragment("", 0, isQuestion);
        }
    }

    @Override
    public void onDialogPhotoSourceClick(boolean isQuestion) {
        if (view != null) {
            view.resolveCameraPermission();
            view.showCameraFragment(1, isQuestion);
        }
    }

    @Override
    public void onDialogGallerySourceClick(boolean isQuestion) {
        if (view != null) {
            view.resolveGalleryPermission();
            view.showGallery(2, isQuestion);
        }
    }

    @Override
    public void onDialogOcrSourceClick(boolean isQuestion) {
        if (view != null) {
            view.resolveCameraPermission();
            view.showCameraFragment(3, isQuestion);
        }
    }

    @Override
    public void onTextDialogOkClick(String string, int type, boolean isQuestion) {

    }

    @Override
    public void onTextDialogCancelClick() {

    }

    @Override
    public void onAnswerTextItemInteraction(String id) {
        if (view != null && model != null) {
            if (id.equals("new")) {
                view.showChooseSourceDialog(false);
            } else {
                Observable<List<TestItem>> readTestItem = model
                        .getTestItem(id)
                        .toObservable();
                readAnswerTextTypingSubscriber = new DisposableObserver<List<TestItem>>() {
                    @Override
                    public void onNext(List<TestItem> testItems) {
                        if (testItems.size() != 0) {
                            try {
                                view.showTextFragment(testItems.get(0).getValue(), testItems.get(0).getType() , testItems.get(0).isQuestion());
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
                };
                readTestItem.subscribe(readAnswerTextTypingSubscriber);
            }
        }
    }
}


