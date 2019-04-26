package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;


import android.graphics.Bitmap;
import android.view.View;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;


public interface ICreateTestFragmentMVPprovider {

    interface IPresenter
    {
        void setView(IFragment view);
        void onAddQuestionClick();
        void onQuestionPressed(String id);
        void onGalleryResult();
        void onBackPressed();
        void onPhotoTaken(String mPath);
        void onPhotoTakingCancelled();
        void onSubjectConfrimed();
        boolean onPhotoPermissionCompatResult(int reqCode, int resCode);
        void rxUnsubscribe();
        void onViewResumed(String s);

        void onItemInteraction(String id);
    }

    interface IFragment
    {
        void setCounterTextView(String s);
        void addQuestion(String id);
        void setQuestion(String value);
        void addAnswer();
        void setAnswer(String id, String param);

        void removeAnswer(String position);
        void showPhotoFragment(Bitmap bitmap);
        void showTextFragment(String text);
        void destroyFragment();
        void showCameraFragment();
        void showGallery();
        void showDeleteAlertDialog();
        void showWhatsSubjectDialog();
        void showChooseSourceDialog();
        void resolveCameraPermission();
        void showToast(String msg);

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
