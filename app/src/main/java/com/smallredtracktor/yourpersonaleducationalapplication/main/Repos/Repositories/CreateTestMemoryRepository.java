package com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Repositories;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.ICreateTestDbApi;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.ParseTextUtil;

import java.util.List;


import io.reactivex.Flowable;
import io.reactivex.Observable;

public class CreateTestMemoryRepository implements ICreateTestRepository {

    private ParseTextUtil util;
    private ICreateTestDbApi testDbApi;

    public CreateTestMemoryRepository(ICreateTestDbApi testDbApi, ParseTextUtil util) {
        this.testDbApi = testDbApi;
        this.util = util;
    }


    @Override
    public Observable<List<TestItem>> writeAllTestItem(List<TestItem> testItems) {
        return null;
    }

    @Override
    public Observable<TestItem> getTestItem(String subj, String id, String ticket) {
        return null;
    }

    @Override
    public Observable<TestItem> deleteTestItem(String subj, String id, String ticket) {
        return null;
    }

    @Override
    public Observable<List<TestItem>> getAllTestItemsForTicket(String subj, String ticket) {
        return null;
    }

    @Override
    public Observable<List<TestItem>> deleteAllTestItemsForTicket(String subj, String ticket) {
        return null;
    }

    @Override
    public Observable<OcrResponseModel> getParsedTextFromFile(String mPath) {
        return  util.getResult(mPath);
    }

    @Override
    public Observable<TicketDataSet> getTicketDataSet(int ticket) {
        return testDbApi.getTicketDataSet(ticket);
    }

    @Override
    public Flowable writeTestItem(String id, boolean isQuestion, int currentTicket, int type, String value) {
        return testDbApi.writeTestItem(id,  isQuestion,  currentTicket, type,  value);
    }

}
