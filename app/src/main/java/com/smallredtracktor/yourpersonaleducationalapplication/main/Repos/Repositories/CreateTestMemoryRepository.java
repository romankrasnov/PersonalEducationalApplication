package com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Repositories;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Answer;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Question;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.ParseTextUtil;

import java.util.List;

import io.reactivex.Observable;

public class CreateTestMemoryRepository implements ICreateTestRepository {

    @Override
    public Observable<Answer> writeAnswer(Answer answer) {
        return null;
    }

    @Override
    public Observable<Question> writeQuestion(Question question) {
        return null;
    }

    @Override
    public Observable<List<Question>> writeAllQuestions(List<Question> questions) {
        return null;
    }

    @Override
    public Observable<List<Answer>> writeAllAnswers(List<Answer> answers) {
        return null;
    }

    @Override
    public Observable<Answer> getAnswer(String subj, String numberByTicket, String ticket) {
        return null;
    }

    @Override
    public Observable<Answer> deleteAnswer(String subj, String numberByTicket, String ticket) {
        return null;
    }

    @Override
    public Observable<Question> getQuestion(String subj, String numberByTicket, String ticket) {
        return null;
    }

    @Override
    public Observable<Answer> deleteQuestion(String subj, String numberByTicket, String ticket) {
        return null;
    }

    @Override
    public Observable<List<Answer>> getAllAnswersForTicket(String subj, String ticket) {
        return null;
    }

    @Override
    public Observable<List<Answer>> deleteAllAnswersForTicket(String subj, String ticket) {
        return null;
    }

    @Override
    public Observable<List<Question>> getAllQuestionsForTicket(String subj, String ticket) {
        return null;
    }

    @Override
    public Observable<List<Question>> deleteAllQuestionsForTicket(String subj, String ticket) {
        return null;
    }

    @Override
    public Observable<OcrResponseModel> getParsedTextFromFile(String mPath) {
        return  ParseTextUtil.getParsedResult(mPath);
    }

}
