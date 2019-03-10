package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Answer;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Question;
import java.util.List;


public interface ICreateTestFragmentMVPprovider {

    interface IPresenter
    {
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
        void onTextSourceChoosed();
        void onPhotoSourceChoosed();
        void onOcrSourceChoosed();
        void onGallerySourceChoosed();
        void onSubjectConfrimed();
        void onSubjectEdited();
    }

    interface IFragment
    {
        void setCounterTextView(String s);
        void addSubjectToQuestionStack(Object o);
        void addSubjectToAnswerStack(Object o);
        void removeSubjectFromQuestionStack(Object o);
        void removeSubjectFromAnswerStack(Object o);
        void showPhotoFragment(Object o);
        void showTextFragment(Object o);
        void destroyPhotoFragment();
        void destroyTextFragment();
        void destroyFragment();
        void showLeftSwipeAnimation();
        void showRightSwipeAnimation();
        void setObjectColour();
        void showCameraFragment();
        void showGallery();
        void showErrorWhileNetworkingMessage();
        void showErrorWhileTakingPhotoMessage();
        void showDeleteAlertDialog();
        void showWhatsSubjectDialog();
        void showChooseSourceDialog();
    }

    interface IModel
    {
        void writeAnswer(Answer answer);
        void writeQuestion(Question question);
        void writeAllQueAns(List<Question> questions, List<Answer> answers);
        Answer getAnswer(String subj,String numberByTicket, String ticket);
        void delesteAnswer(String subj,String numberByTicket, String ticket);
        Question getQuestion(String subj,String numberByTicket, String ticket);
        void deleteQuestion(String subj,String numberByTicket, String ticket);
        List<Answer> getAllAnswersForTicket(String subj, String ticket);
        void deleteAllAnswersForTicket(String subj, String ticket);
        List<Question> getAllQuestionsForTicket(String subj, String ticket);
        void deleteAllQuestionsForTicket(String subj, String ticket);
    }
}
