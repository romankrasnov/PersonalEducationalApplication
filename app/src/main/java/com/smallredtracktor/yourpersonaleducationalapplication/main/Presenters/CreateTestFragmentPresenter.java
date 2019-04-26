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
    private boolean isQuestion;
    private boolean isOcr;
    private int type;
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
            isQuestion = true;
            view.showChooseSourceDialog();
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
                            view.showTextFragment(testItems.get(0).getValue());
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
    public void onSubjectConfrimed() {

    }


    @Override
    public void onPhotoTaken(String mPath) {
        if (view != null && model != null) {
            String id = UniqueDigit.getUnique();
            if(isQuestion) {
                view.addQuestion(id);
                view.setQuestion("loading");
            }else {
                    view.setAnswer(null, "loading");
                }
            readPhotoTakingSubscriber = new DisposableObserver<List<TestItem>>() {
                    @Override
                    public void onNext(List<TestItem> testItems) {
                        if (testItems.size() != 0) {
                            try {
                                if(isQuestion)
                                {
                                    view.setQuestion(testItems.get(0).getValue());
                                } else
                                    {
                                        view.setAnswer(id, testItems.get(0).getValue());
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
            Observable<List<TestItem>> readTestItem;
            if(isOcr)
            {                                                                                                                                                                                                                                                                                                         
                 // noinspection unchecked
                readTestItem = model
                        .getParsedTextResult(mPath)
                        .flatMap((Function<OcrResponseModel, ObservableSource<List<TestItem>>>) ocrResponseModel -> {
                            model.writeTestItem(id,isQuestion,currentTicket,type, ocrResponseModel.getText());
                            return model.getTestItem(id).toObservable();
                        });
            }
            else {
                // noinspection unchecked
                model.writeTestItem(id,isQuestion,currentTicket,type, mPath);
                readTestItem = model
                        .getTestItem(id)
                        .toObservable();
            }
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
    public void rxUnsubscribe()
    {   if (readPhotoTakingSubscriber != null) {
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
    public void onDialogTextSourceClick() {
        if (view != null) {
            type = 0;
            view.showTextFragment("");
        }
    }

    @Override
    public void onDialogPhotoSourceClick() {
        if (view != null) {
            isOcr = false;
            type = 1;
            view.resolveCameraPermission();
            view.showCameraFragment();
        }
    }

    @Override
    public void onDialogGallerySourceClick() {
        if (view != null) {
            view.showGallery();
            type = 2;
        }
    }

    @Override
    public void onDialogOcrSourceClick() {
        if (view != null) {
            isOcr = true;
            type = 3;
            view.resolveCameraPermission();
            view.showCameraFragment();
        }
    }

    @Override
    public void onTextDialogOkClick(String string) {

    }

    @Override
    public void onTextDialogCancelClick() {

    }

    @Override
    public void onItemInteraction(String id) {
            if (view != null && model != null) {
                isQuestion = false;
                if (id == "first" || id == "new") {
                    view.showChooseSourceDialog();
                }else
                {
                    Observable<List<TestItem>> readTestItem = model
                            .getTestItem(id)
                            .toObservable();
                    readAnswerTextTypingSubscriber = new DisposableObserver<List<TestItem>>() {
                        @Override
                        public void onNext(List<TestItem> testItems) {
                            if (testItems.size() != 0) {
                                try {
                                    view.showTextFragment(testItems.get(0).getValue());
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
                    readTestItem.subscribe(readAnswerTextTypingSubscriber);
                }
            }
        }
}
