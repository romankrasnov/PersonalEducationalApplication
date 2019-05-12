package com.smallredtracktor.yourpersonaleducationalapplication.main.Models;

import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.ICreateTestRootDbApi;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IRootCreateTestFragmentMVPprovider;

import java.util.List;

public class CreateTestRootFragmentModel implements IRootCreateTestFragmentMVPprovider.IModel {


    private final ICreateTestRootDbApi dbApi;

    public CreateTestRootFragmentModel(ICreateTestRootDbApi dbApi) {
        this.dbApi = dbApi;
    }

    @Override
    public void writeTickets(List<String> strings, String id) {
        dbApi.writeTickets(strings, id);
    }

    @Override
    public void writeSubject(String id, String text) {
        dbApi.writeSubject(id, text);
    }
}
