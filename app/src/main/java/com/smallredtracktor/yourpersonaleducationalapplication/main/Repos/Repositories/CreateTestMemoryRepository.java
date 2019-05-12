package com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Repositories;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.ICreateTestDbApi;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataStorages.ILocalStorage;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.ParseTextUtil;

import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.Maybe;


public class CreateTestMemoryRepository implements ICreateTestRepository {

    private ParseTextUtil util;
    private ICreateTestDbApi testDbApi;
    private ILocalStorage localStorage;

    public CreateTestMemoryRepository(ICreateTestDbApi testDbApi, ParseTextUtil util, ILocalStorage localStorage) {
        this.testDbApi = testDbApi;
        this.util = util;
        this.localStorage = localStorage;
    }


    @Override
    public Flowable<List<TestItem>> getTestItem(String id) {
        return testDbApi.itemById(id);
    }

    @Override
    public void deleteTestItem(String id) {
        getTestItem(id)
                .toObservable()
                .doOnNext(testItems -> {
                    try {
                        if (testItems.get(0).getId() != null) {
                            int type = testItems.get(0).getType();
                            if (type == 1 || type == 2) {
                                String filepath = testItems.get(0).getValue();
                                try {
                                    deleteFile(filepath);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            testDbApi.deleteTestItem(id);
                        }
                    } catch (ArrayIndexOutOfBoundsException e)
                    {
                        //Caused by Realm live objects
                    }

        }).subscribe();
    }

    @Override
    public void deleteFile(String filepath)
    {
        localStorage.deleteFile(filepath);
    }

    @Override
    public Maybe<OcrResponseModel> getParsedTextFromFile(String mPath) {
        return  util.getResult(mPath);
    }

    @Override
    public void writeTestItem(String id, boolean isQuestion, String currentTicket, int type, String value) {
        testDbApi.updateTestItem(id,  isQuestion,  currentTicket, type,  value);
    }
}
