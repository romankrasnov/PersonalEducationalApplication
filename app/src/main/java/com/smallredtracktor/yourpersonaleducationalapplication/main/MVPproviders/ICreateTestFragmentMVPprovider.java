package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;


import android.net.Uri;
import android.view.View;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Answer;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.ApplicationPhoto;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Question;

import java.util.List;


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
        void onClearCLick();
        void onDoneClick();
        void onObjectLongPressed();
        void onCameraResult();
        void onGalleryResult();
        void onSubjectConfrimed();
        void onSubjectEdited();
        void onVideoRecorded();
        void onPhotoTaken(Uri mPath);
        boolean onPhotoPermissionCompatResult(int reqCode, int resCode);

        void onPhotoTakingCancelled();
    }

    interface IFragment
    {
        void setCounterTextView(String s);
        void addSubjectToQuestionStack(View v);
        void addSubjectToAnswerStack(View v);
        void removeSubjectFromQuestionStack(int position);
        void removeSubjectFromAnswerStack(int position);
        void showPhotoFragment(ApplicationPhoto o);
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
    }

    interface IModel
    {
        void writeAnswer(Answer answer);
        void writeQuestion(Question question);
        void writeAllQueAns(List<Question> questions, List<Answer> answers);
        Answer getAnswer(String subj,String numberByTicket, String ticket);
        void deleteAnswer(String subj,String numberByTicket, String ticket);
        Question getQuestion(String subj,String numberByTicket, String ticket);
        void deleteQuestion(String subj,String numberByTicket, String ticket);
        List<Answer> getAllAnswersForTicket(String subj, String ticket);
        void deleteAllAnswersForTicket(String subj, String ticket);
        List<Question> getAllQuestionsForTicket(String subj, String ticket);
        void deleteAllQuestionsForTicket(String subj, String ticket);
    }
}
