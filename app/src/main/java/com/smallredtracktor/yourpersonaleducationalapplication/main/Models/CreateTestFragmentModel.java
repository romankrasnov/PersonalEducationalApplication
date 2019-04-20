package com.smallredtracktor.yourpersonaleducationalapplication.main.Models;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;


public class CreateTestFragmentModel implements ICreateTestFragmentMVPprovider.IModel {

    private ICreateTestRepository repository;

    public CreateTestFragmentModel(ICreateTestRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<TestItem>> writeAllTestItem(List<TestItem> testItems) {
        return null;
    }

    @Override
    public Flowable<List<TestItem>> getTestItem(String id) {
        return repository.getTestItem(id);
    }

    @Override
    public Observable<TestItem> deleteTestItem(String id) {
        return null;
    }

    @Override
    public Observable<List<TestItem>> getAllTestItemsForTicket(String ticket) {
        return null;
    }

    @Override
    public Observable<List<TestItem>> deleteAllTestItemsForTicket(String ticket) {
        return null;
    }

    @Override
    public Observable<TicketDataSet> getTicketDataSet(int ticket) {
       return repository.getTicketDataSet(ticket);
    }

    @Override
    public Observable<OcrResponseModel> getParsedTextResult(String path) {
        return repository.getParsedTextFromFile(path);
    }

    @Override
    public void writeTestItem(String id, boolean isQuestion, int currentTicket, int type, String value) {
        repository.writeTestItem(id,  isQuestion,  currentTicket, type,  value);
    }


}
