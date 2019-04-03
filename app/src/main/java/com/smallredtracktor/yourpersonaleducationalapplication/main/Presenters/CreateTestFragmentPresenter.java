package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;


import javax.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


import static android.app.Activity.RESULT_OK;

public class CreateTestFragmentPresenter implements
        ICreateTestFragmentMVPprovider.IPresenter,
      ChooseSourceDialog.ChooseSourceDialogListener{

    @Nullable
    private ICreateTestFragmentMVPprovider.IFragment view;
    @Nullable
    private ICreateTestFragmentMVPprovider.IModel model;

    private DisposableObserver<OcrResponseModel> ocrObserver;

    private boolean isQuestion;
    private boolean isOcr;
    private int currentTicket = 1;
    private DisposableObserver<TestItem> itemWriteObserver;

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
    public void onObjectLongPressed(String id) {

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
            if(isOcr)
            {
                String id = UniqueDigit.getUnique();
                if(isQuestion) {
                    view.addSubjectToQuestionStack(id);
                }else
                {
                    view.addSubjectToAnswerStack(id);
                }
                Observable<OcrResponseModel> ocrObservable = model.getParsedTextResult(mPath);
                ocrObserver = new DisposableObserver<OcrResponseModel>() {
                    @Override
                    public void onNext(OcrResponseModel ocrResponseModel) {
                        String s = ocrResponseModel.getText();
                        TestItem item = new TestItem();
                        item.setId(id);
                        item.setQuestion(isQuestion);
                        item.setTicketId(currentTicket);
                        item.setType(3);
                        item.setValue(s);
                        Observable<TestItem> writeTestItemObservable = model.writeTestItem(item);
                        itemWriteObserver = new DisposableObserver<TestItem>()
                            {
                                @Override
                                public void onNext(TestItem testItem) {
                                   view.showToast("Ready");
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {
                                }
                            };
                        writeTestItemObservable.subscribe(itemWriteObserver);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                };


                ocrObservable.subscribe(ocrObserver);
            }
            else {

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
    {   if (ocrObserver != null) {
        if (!ocrObserver.isDisposed()) {
            ocrObserver.dispose();
            itemWriteObserver.dispose();
        }
    }
}

    @Override
    public void onObjectLongPressed(int id) {

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
