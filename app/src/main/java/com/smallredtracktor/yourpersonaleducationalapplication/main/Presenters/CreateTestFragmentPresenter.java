package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Answer;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Question;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;


import javax.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


import static android.app.Activity.RESULT_OK;

public class CreateTestFragmentPresenter implements
        ICreateTestFragmentMVPprovider.IPresenter,
        ChooseSourceDialog.ChooseSourceDialogListener {

    @Nullable
    private ICreateTestFragmentMVPprovider.IFragment view;
    @Nullable
    private ICreateTestFragmentMVPprovider.IModel model;

    DisposableObserver<OcrResponseModel> ocrObserver;

    private boolean isQuestion;
    private boolean isOcr;
    private int currentTicket = 0;

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
            currentTicket++;
            view.setCounterTextView(String.valueOf(currentTicket));
        }
    }

    @Override
    public void onSwipeLeft() {
        if (view != null) {
            currentTicket--;
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
    public void onObjectLongPressed() {

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
                Observable<OcrResponseModel> observable = model.getParsedTextResult(mPath);
                ocrObserver = new DisposableObserver<OcrResponseModel>() {
                    @Override
                    public void onNext(OcrResponseModel ocrResponseModel) {
                        String s = ocrResponseModel.getText();
                        if (isQuestion)
                        {
                            int position = view.addSubjectToQuestionStack();
                            Question question = new Question();
                            question.setValue(s);
                            question.setType(3);
                            question.setUserStackNumber(position + 1);
                            question.setTicketId(currentTicket);
                            model.getWriteQuestionResult(question);
                        }
                        else {
                            int position = view.addSubjectToAnswerStack();
                            Answer answer = new Answer();
                            answer.setValue(s);
                            answer.setQuestionId(1);    //ебота, пересмотреть, вопрос от ответа
                                                        // в предметной области не отличается,
                                                        // всё нахуй в одну сущность ёбана
                            answer.setUserStackNumber(position + 1);
                            answer.setType(3);
                        }
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                };


                observable.subscribe(ocrObserver);
            }
            else {
                if (isQuestion)
                {
 //               model.writeQuestion(new Question());
                }
                    else
                        {
    //                model.writeAnswer(new Answer());
                        }
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
        }
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
