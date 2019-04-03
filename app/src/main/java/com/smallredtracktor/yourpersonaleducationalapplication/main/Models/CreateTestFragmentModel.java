package com.smallredtracktor.yourpersonaleducationalapplication.main.Models;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;

import io.reactivex.Observable;



public class CreateTestFragmentModel implements ICreateTestFragmentMVPprovider.IModel {

    private ICreateTestRepository repository;

    public CreateTestFragmentModel(ICreateTestRepository repository) {
        this.repository = repository;
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
    public Observable<TestItem> writeTestItem(TestItem item) {
        return repository.writeTestItem(item);
    }


}
