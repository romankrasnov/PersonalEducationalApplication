package com.smallredtracktor.yourpersonaleducationalapplication.main.Models;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Answer;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Question;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;

import java.util.List;

public class CreateTestFragmentModel implements ICreateTestFragmentMVPprovider.IModel {

    public CreateTestFragmentModel(ICreateTestRepository repository) {
    }

    @Override
    public void writeAnswer(Answer answer) {

    }

    @Override
    public void writeQuestion(Question question) {

    }

    @Override
    public void writeAllQueAns(List<Question> questions, List<Answer> answers) {

    }

    @Override
    public Answer getAnswer(String subj, String numberByTicket, String ticket) {
        return null;
    }

    @Override
    public void deleteAnswer(String subj, String numberByTicket, String ticket) {

    }

    @Override
    public Question getQuestion(String subj, String numberByTicket, String ticket) {
        return null;
    }

    @Override
    public void deleteQuestion(String subj, String numberByTicket, String ticket) {

    }

    @Override
    public List<Answer> getAllAnswersForTicket(String subj, String ticket) {
        return null;
    }

    @Override
    public void deleteAllAnswersForTicket(String subj, String ticket) {

    }

    @Override
    public List<Question> getAllQuestionsForTicket(String subj, String ticket) {
        return null;
    }

    @Override
    public void deleteAllQuestionsForTicket(String subj, String ticket) {

    }
}
