package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;


public interface ICreateTestFragmentMVPprovider {

    interface IPresenter
    {
        void setView(IFragment view);
        void onAddQuestionClick();
        void onQuestionPressed(String id);
        void onGalleryResult(String path, int type, boolean isQuestion);
        void onBackPressed();
        void onPhotoTaken(String mPath, int type, boolean isQuestion);
        void onAcceptSubject();
        void rxUnsubscribe();
        void onViewResumed(String s);
        void onAnswerFragmentInteraction(String id);
        void onAnswerLongClick(String id);
        boolean onQuestionLongPressed(String id);
    }

    interface IFragment
    {
        void setCounterTextView(String s);
        void showToast(String msg);
        void setTextQuestion(String id, int type, String value);
        void setPhotoQuestion(String id, int type, String content);
        void setCurrentAnswer(String id, int type, String value);
        void addNewAnswer();
        void removeAnswer(String id);
        void showPhotoFragment(String id, String value, int type, boolean question);
        void showTextFragment(String id, String text, int type, boolean isQuestion);
        void showCameraFragment(int type, boolean isQuestion);
        void showGallery(int type, boolean isQuestion);
        void deleteQuestion();
        void showWhatsSubjectDialog();
        void showChooseSourceDialog(boolean b);
        void resolveCameraPermission();
    }

    interface IModel
    {
        Flowable<List<TestItem>> getTestItem(String id);
        void deleteTestItem(String id);
        Observable<OcrResponseModel> getParsedTextResult(String mPath);
        void updateTestItem(String id, boolean isQuestion, int currentTicket, int type, String value);
    }
}
