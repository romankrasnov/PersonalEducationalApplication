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
        void onSwipeTop();
        void onSwipeRight();
        void onSwipeLeft();
        void onSwipeBottom();
        void onAddQuestionClick();
        void onAddAnswerClick();
        void onClearClick();
        void onDoneClick();
        void onObjectLongPressed(String id);
        void onGalleryResult();
        void onBackPressed();
        void onPhotoTaken(String mPath);
        void onPhotoTakingCancelled();
        void onSubjectConfrimed();
        void onSubjectEdited();
        void onVideoRecorded();
        boolean onPhotoPermissionCompatResult(int reqCode, int resCode);
        void rxUnsubscribe();
    }

    interface IFragment
    {
        void setCounterTextView(String s);


        void addSubjectToQuestionStack(String id);
        void addSubjectToAnswerStack(String id);
        void removeSubjectFromQuestionStack(String position);
        void removeSubjectFromAnswerStack(String position);
        void showPhotoFragment(Bitmap bitmap);
        void showTextFragment(String text);
        void destroyPhotoFragment();
        void destroyTextFragment();
        void destroyFragment();
        void showLeftSwipeAnimation();
        void showRightSwipeAnimation();
        void setObjectColour(View v);
        void showCameraFragment();
        void showGallery();
        void showErrorWhileNetworkingMessage();
        void showErrorWhileTakingPhotoMessage();
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
