package com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Answer;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Question;

import java.util.List;

import io.reactivex.Observable;

public interface ICreateTestRepository  {
    Observable<Answer> writeAnswer(Answer answer);

    Observable<Question> writeQuestion(Question question);

    Observable<List<Question>> writeAllQuestions(List<Question> questions);

    Observable<List<Answer>> writeAllAnswers(List<Answer> answers);

    Observable<Answer> getAnswer(String subj, String numberByTicket, String ticket);

    Observable<Answer> deleteAnswer(String subj, String numberByTicket, String ticket);

    Observable<Question> getQuestion(String subj, String numberByTicket, String ticket);

    Observable<Answer> deleteQuestion(String subj, String numberByTicket, String ticket);

    Observable<List<Answer>> getAllAnswersForTicket(String subj, String ticket);

    Observable<List<Answer>> deleteAllAnswersForTicket(String subj, String ticket);

    Observable<List<Question>> getAllQuestionsForTicket(String subj, String ticket);

    Observable<List<Question>> deleteAllQuestionsForTicket(String subj, String ticket);

    Observable<OcrResponseModel> getParsedTextFromFile(String mPath);
}
