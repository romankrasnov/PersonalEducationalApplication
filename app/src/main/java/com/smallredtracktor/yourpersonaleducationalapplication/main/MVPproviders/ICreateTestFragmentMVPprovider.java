package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.Abstract.IAbstractModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.Abstract.IAbstractPresenter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.Abstract.IAbstractView;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;


public interface ICreateTestFragmentMVPprovider {

    interface IPresenter extends IAbstractPresenter
    {
        void setView(IFragment view);
        void onAddQuestionClick();
        void onQuestionPressed(String id);
        void onGalleryResult(Uri path, int type, boolean isQuestion);
        void onBackPressed();
        void onPhotoTaken(String mPath, int type, boolean isQuestion);
        void rxUnsubscribe();
        void onViewResumed(String s, String ticketId);
        void onAnswerFragmentInteraction(String id);
        void onAnswerLongClick(String id);
        boolean onQuestionLongPressed(String id);
    }

    interface IFragment extends IAbstractView
    {
        void setCounterTextView(String s);
        void showToast(String msg);
        void setTextQuestion(String id, int type, String value);
        void setPhotoQuestion(String id, int type, Bitmap content);
        void setCurrentAnswer(String id, int type, String value);
        void addNewAnswer();
        void removeAnswer(String id);
        void showPhotoFragment(String id, String value, int type, boolean question);
        void showTextFragment(String id, String text, int type, boolean isQuestion);
        void showCameraFragment(Intent intent, int type, boolean isQuestion, String path);
        void showGallery(int type, boolean isQuestion);
        void deleteQuestion();
        void showChooseSourceDialog(boolean b);
        void resolveCameraPermission();
        void switchAnswerViewPagerMode();
    }

    interface IModel extends IAbstractModel
    {
        Flowable<List<TestItem>> getTestItem(String id);
        void deleteTestItem(String id);
        void deleteFile(String filepath);
        Maybe<OcrResponseModel> getParsedTextResult(String mPath);
        void updateTestItem(String id, boolean isQuestion, String currentTicket, int type, String value);
    }
}
