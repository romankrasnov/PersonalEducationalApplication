package com.smallredtracktor.yourpersonaleducationalapplication.main.Models;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Question;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;

import io.reactivex.Observable;



public class CreateTestFragmentModel implements ICreateTestFragmentMVPprovider.IModel {

    private ICreateTestRepository repository;

    public CreateTestFragmentModel(ICreateTestRepository repository) {
        this.repository = repository;
    }

    @Override
        public Observable<Question> getWriteQuestionResult(Question question) {
        return repository.writeQuestion(question);
    }

    @Override
        public Observable<OcrResponseModel> getParsedTextResult(String path) {
        return repository.getParsedTextFromFile(path);
    }


}
