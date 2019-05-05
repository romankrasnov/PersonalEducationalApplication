package com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Repositories;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.ICreateTestDbApi;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.ParseTextUtil;

import java.util.List;


import io.reactivex.Flowable;
import io.reactivex.Single;

public class CreateTestMemoryRepository implements ICreateTestRepository {

    private ParseTextUtil util;
    private ICreateTestDbApi testDbApi;

    public CreateTestMemoryRepository(ICreateTestDbApi testDbApi, ParseTextUtil util) {
        this.testDbApi = testDbApi;
        this.util = util;
    }


    @Override
    public Flowable<List<TestItem>> getTestItem(String id) {
        return testDbApi.itemById(id);
    }

    @Override
    public void deleteTestItem(String id) {
        testDbApi.deleteTestItem(id);
    }

    @Override
    public Single<OcrResponseModel> getParsedTextFromFile(String mPath) {
        return  util.getResult(mPath);
    }

    @Override
    public void writeTestItem(String id, boolean isQuestion, int currentTicket, int type, String value) {
        testDbApi.updateTestItem(id,  isQuestion,  currentTicket, type,  value);
    }
}
