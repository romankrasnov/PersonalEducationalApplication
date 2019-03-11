package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;



import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;

import javax.annotation.Nullable;

import static android.app.Activity.RESULT_OK;

public class CreateTestFragmentPresenter implements ICreateTestFragmentMVPprovider.IPresenter {

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
            view.showRightSwipeAnimation();
        }
    }

    @Override
    public void onSwipeLeft() {
        if (view != null) {
            view.showLeftSwipeAnimation();
        }
    }

    @Override
    public void onSwipeBottom() {

    }

    @Override
    public void onAddQuestionClick() {
        if (view != null) {
            view.showCameraFragment();
        }
    }

    @Override
    public void onAddAnswerClick() {

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
    public void onTextSourceChoosed() {

    }

    @Override
    public void onPhotoSourceChoosed() {

    }

    @Override
    public void onOcrSourceChoosed() {

    }

    @Override
    public void onGallerySourceChoosed() {

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
    public void onPhotoTaken(byte[] bytes, String filePath) {

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
}
