package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;


import android.net.Uri;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;


import javax.annotation.Nullable;

import static android.app.Activity.RESULT_OK;

public class CreateTestFragmentPresenter implements
        ICreateTestFragmentMVPprovider.IPresenter,
        ChooseSourceDialog.ChooseSourceDialogListener {

    @Nullable
    private ICreateTestFragmentMVPprovider.IFragment view;

    public CreateTestFragmentPresenter(ICreateTestFragmentMVPprovider.IModel model) {
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
            view.setCounterTextView("right");
        }
    }

    @Override
    public void onSwipeLeft() {
        if (view != null) {
            view.setCounterTextView("left");
        }
    }

    @Override
    public void onSwipeBottom() {

    }

    @Override
    public void onAddQuestionClick() {
        if (view != null) {
            view.showChooseSourceDialog();
        }
    }

    @Override
    public void onAddAnswerClick() {
        if (view != null) {
            view.showChooseSourceDialog();
        }
    }

    @Override
    public void onClearCLick() {

    }

    @Override
    public void onDoneClick() {

    }

    @Override
    public void onObjectLongPressed() {

    }

    @Override
    public void onCameraResult() {

    }

    @Override
    public void onGalleryResult() {

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
    public void onPhotoTaken(Uri mPath) {

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
            view.resolveCameraPermission();
            view.showCameraFragment();
        }
    }

    @Override
    public void onDialogGallerySourceClick() {

    }

    @Override
    public void onDialogOcrSourceClick() {

    }
}
