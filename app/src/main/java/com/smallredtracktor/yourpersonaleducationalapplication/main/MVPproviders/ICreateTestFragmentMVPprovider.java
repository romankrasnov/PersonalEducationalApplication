package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.MotionEvent;

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
        void onViewDestroyed();
        void onViewResumed(String s, String ticketId);
        void onAnswerViewSwipe(String id);
        void onQuestionLongPressed(String id);
        void onAnswerPageSelected(int page, int count);
        void onViewModeChanged(boolean isFullScreenMode);
        void onAnswerFragmentClick(String id);
        void onAnswerScroll(String id, MotionEvent e2);
        void onAnswerDown(String id, MotionEvent e);
        void onAnswerFragmentUp(String id, MotionEvent event);
        void onTextAnswerLongClick(String id);
        void onTextAnswerDoubleTap(String id);
        void onRequestPermissionsResult(int requestCode, boolean isQuestion);
    }

    interface IFragment extends IAbstractView
    {
        void setCounterTextView(String count);
        void showToast(String msg);
        void setTextQuestion(String id, int type, String value);
        void setPhotoQuestion(String id, int type, Bitmap content);
        void setCurrentAnswer(String id, int type, String value);
        void addNewAnswer();
        void removeAnswer(String id);
        void showPhotoFragment(String id, Bitmap value, int type, boolean question);
        void showTextFragment(String id, String text, int type, boolean isQuestion);
        void showCameraFragment(Intent intent, int type, boolean isQuestion, String path);
        void showGallery(int type, boolean isQuestion, Intent intent);
        void deleteQuestion();
        void showChooseSourceDialog(boolean isQuestion);
        void resolveCameraPermission(int type, boolean isQuestion);
        void setCurrentAnswerItem(int i);
        void switchPagerToFullScreen();
        void switchPagerToSmallView();
        void animateAnswer(String id, float rawY);
        void calculateAnswerScroll(String id, float rawY);
        void scrollAnswer(String id, float rawY);
        void notifyAdapterViewModeChanged(boolean isFullScreenMode);
        void resetAnswerTransition(String id);
        void showOcrDrawingDialog(String id, Bitmap fullBitmap, String path, boolean isQuestion);
        void setOcrDrawingDialogMode(boolean mode);
        void undoDrawingDialogViewPoint();
        void redoDrawingDialogViewPoint();
        void closeOcrDrawingDialog();
        void addPointToDrawerView(float x, float y);
        void recomputeLastDrawerViewPoint(float x, float y);
        void switchTextItemSwipeMode(String id);
    }

    interface IModel extends IAbstractModel
    {
        Flowable<List<TestItem>> getTestItem(String id);
        void deleteTestItem(String id);
        void deleteFile(String filepath);
        Maybe<OcrResponseModel> getParsedTextResult(Bitmap bitmap);
        void updateTestItem(String id, boolean isQuestion, String currentTicket, int type, String value);
    }
}
