package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;



import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Answer;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Question;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.ParseTextUtil;


import javax.annotation.Nullable;

import static android.app.Activity.RESULT_OK;

public class CreateTestFragmentPresenter implements
        ICreateTestFragmentMVPprovider.IPresenter,
        ChooseSourceDialog.ChooseSourceDialogListener {

    @Nullable
    private ICreateTestFragmentMVPprovider.IFragment view;
    @Nullable
    private ICreateTestFragmentMVPprovider.IModel model;


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
                String parsed = ParseTextUtil.getParsedResult(mPath);
                if (isQuestion)
                {
                model.writeQuestion(new Question());
                }
            else {
                model.writeAnswer(new Answer());
                 }
            }
            else {
                if (isQuestion)
                {
                model.writeQuestion(new Question());
                }
                    else
                        {
                    model.writeAnswer(new Answer());
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
