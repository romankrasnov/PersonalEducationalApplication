package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;


import java.util.List;

import javax.annotation.Nullable;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;


import static android.app.Activity.RESULT_OK;

public class CreateTestFragmentPresenter implements
        ICreateTestFragmentMVPprovider.IPresenter,
      ChooseSourceDialog.ChooseSourceDialogListener{

    @Nullable
    private ICreateTestFragmentMVPprovider.IFragment view;
    @Nullable
    private ICreateTestFragmentMVPprovider.IModel model;

    private DisposableObserver<List<TestItem>> writeSubscriber;

    private boolean isQuestion;
    private boolean isOcr;
    private int currentTicket = 1;



    public CreateTestFragmentPresenter(ICreateTestFragmentMVPprovider.IModel model) {
        this.model = model;
    }


    @Override
    public void setView(ICreateTestFragmentMVPprovider.IFragment view) {
        this.view = view;
    }


    @Override
    public void onSwipeTop() {

    }

    @Override
    public void onSwipeRight() {
        if (view != null) {
            if (currentTicket > 1) {
                currentTicket--;
                view.setCounterTextView(String.valueOf(currentTicket));
            }
        }
    }

    @Override
    public void onSwipeLeft() {
        if (view != null) {
            currentTicket++;
            view.setCounterTextView(String.valueOf(currentTicket));
        }
    }

    @Override
    public void onSwipeBottom() {
    }

    @Override
    public void onAddQuestionClick() {
        if (view != null) {
            isQuestion = true;
            view.showChooseSourceDialog();
        }
    }

    @Override
    public void onAddAnswerClick() {
        if (view != null) {
            isQuestion = false;
            view.showChooseSourceDialog();
        }
    }

    @Override
    public void onClearClick() {

    }

    @Override
    public void onDoneClick() {

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
    public void onSubjectEdited() {

    }

    @Override
    public void onVideoRecorded() {

    }

    @Override
    public void onPhotoTaken(String mPath) {
        if (view != null && model != null) {
            String id = UniqueDigit.getUnique();
            if(isQuestion) {
                view.addSubjectToQuestionStack(id);
            }else {
                view.addSubjectToAnswerStack(id);
                }
                writeSubscriber = new DisposableObserver<List<TestItem>>() {
                    @Override
                    public void onNext(List<TestItem> testItems) {
                        view.setCounterTextView(testItems.get(0).getValue());
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showToast("Error while networking");
                    }

                    @Override
                    public void onComplete() {
                    }
                };
            if(isOcr)
            {
                 // noinspection unchecked
                Observable<List<TestItem>> writeTestItem = model
                        .getParsedTextResult(mPath)
                        .flatMap((Function<OcrResponseModel, ObservableSource<List<TestItem>>>)
                                ocrResponseModel -> model
                                        .writeTestItem(id,isQuestion,currentTicket,3, ocrResponseModel.getText())
                                        .toObservable());
                writeTestItem
                        .subscribe(writeSubscriber);
            }
            else {
                // noinspection unchecked
                Observable<List<TestItem>> writeTestItem = model
                        .writeTestItem(id, isQuestion,currentTicket, 1, mPath)
                        .toObservable();
                writeTestItem
                        .subscribe(writeSubscriber);
            }
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
    {   if (writeSubscriber != null) {
        if (!writeSubscriber.isDisposed()) {
            writeSubscriber.dispose();
        }
    }
}

    @Override
    public void onObjectLongPressed(String id) {
        if (view != null) {
            view.setCounterTextView(id);
        }
    }

    @Override
    public void onPhotoTakingCancelled() {

    }

    @Override
    public void onDialogTextSourceClick() {

    }

    @Override
    public void onDialogPhotoSourceClick() {
        if (view != null) {
            isOcr = false;
            view.resolveCameraPermission();
            view.showCameraFragment();
        }
    }

    @Override
    public void onDialogGallerySourceClick() {

    }

    @Override
    public void onDialogOcrSourceClick() {
        if (view != null) {
            isOcr = true;
            view.resolveCameraPermission();
            view.showCameraFragment();
        }
    }
}
