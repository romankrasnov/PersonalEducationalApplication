package com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ICreateTestRepository  {

    Flowable<List<TestItem>> getTestItem(String id);
    void deleteTestItem(String id);
    Single<OcrResponseModel> getParsedTextFromFile(String mPath);
    void writeTestItem(String id, boolean isQuestion, int currentTicket, int type, String value);
}
