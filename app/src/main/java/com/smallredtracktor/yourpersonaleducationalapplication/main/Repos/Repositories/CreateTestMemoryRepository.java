package com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Repositories;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.ICreateTestDbApi;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.ParseTextUtil;

import java.util.List;




import io.reactivex.Observable;

public class CreateTestMemoryRepository implements ICreateTestRepository {

    private ICreateTestDbApi testDbApi;

    public CreateTestMemoryRepository(ICreateTestDbApi testDbApi) {
        this.testDbApi = testDbApi;
    }



    @Override
    public Observable<TestItem> writeTestItem(TestItem testItem) {
        return testDbApi.writeTestItem(testItem);
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
        return  ParseTextUtil.getParsedResult(mPath);
    }

    @Override
    public Observable<TicketDataSet> getTicketDataSet(int ticket) {
        return testDbApi.getTicketDataSet(ticket);
    }

}
