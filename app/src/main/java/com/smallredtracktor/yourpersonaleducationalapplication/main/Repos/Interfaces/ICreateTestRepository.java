package com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface ICreateTestRepository  {

    Flowable<List<TestItem>> getTestItem(String id);
    void deleteTestItem(String id);
    void deleteFile(String filepath);
    Maybe<OcrResponseModel> getParsedTextFromFile(String mPath);
    void writeTestItem(String id, boolean isQuestion, String currentTicket, int type, String value);
}
