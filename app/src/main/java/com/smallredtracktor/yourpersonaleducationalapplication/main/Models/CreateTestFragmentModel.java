package com.smallredtracktor.yourpersonaleducationalapplication.main.Models;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class CreateTestFragmentModel implements ICreateTestFragmentMVPprovider.IModel {

    private ICreateTestRepository repository;

    public CreateTestFragmentModel(ICreateTestRepository repository) {
        this.repository = repository;
    }


    @Override
    public Flowable<List<TestItem>> getTestItem(String id) {
        return repository.getTestItem(id);
    }

    @Override
    public void deleteTestItem(String id) {
        repository.deleteTestItem(id);
    }


    @Override
    public Single<OcrResponseModel> getParsedTextResult(String path) {
        return repository.getParsedTextFromFile(path);
    }

    @Override
    public void updateTestItem(String id, boolean isQuestion, int currentTicket, int type, String value) {
        repository.writeTestItem(id,  isQuestion,  currentTicket, type,  value);
    }
}
