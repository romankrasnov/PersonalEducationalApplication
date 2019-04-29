package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;


import android.graphics.Bitmap;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;


public interface ICreateTestFragmentMVPprovider {

    interface IPresenter
    {
        void setView(IFragment view);
        void onAddQuestionClick();
        void onQuestionPressed(String id);
        void onGalleryResult();
        void onBackPressed();
        void onPhotoTaken(String mPath, int type, boolean isQuestion);
        void onPhotoTakingCancelled();
        void onAcceptSubject();
        boolean onPhotoPermissionCompatResult(int reqCode, int resCode);
        void rxUnsubscribe();
        void onViewResumed(String s);
        void onAnswerTextItemInteraction(String id);
    }

    interface IFragment
    {
        void setCounterTextView(String s);

        void showToast(String msg);

        void addQuestion(String id);
        void setQuestion(String value);

        void setCurrentAnswer(String id, int type, String param);
        void addNewAnswer();

        void removeAnswer(String position);

        void showPhotoFragment(Bitmap bitmap);
        void showTextFragment(String text, int i, boolean isQuestion);
        void destroyFragment();
        void showCameraFragment(int i, boolean isQuestion);
        void showGallery(int i, boolean isQuestion);
        void showDeleteAlertDialog();
        void showWhatsSubjectDialog();
        void showChooseSourceDialog(boolean b);

        void resolveCameraPermission();
        void resolveGalleryPermission();


    }

    interface IModel
    {
        Observable<List<TestItem>> writeAllTestItem(List<TestItem> testItems);
        Flowable<List<TestItem>> getTestItem(String id);
        Observable<TestItem> deleteTestItem(String id);
        Observable<List<TestItem>> getAllTestItemsForTicket(String ticket);
        Observable<List<TestItem>> deleteAllTestItemsForTicket(String ticket);
        Observable<TicketDataSet> getTicketDataSet(int ticket);
        Observable<OcrResponseModel> getParsedTextResult(String mPath);
        void writeTestItem(String id, boolean isQuestion, int currentTicket, int type, String value);
    }
}
