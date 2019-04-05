package com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface ICreateTestRepository  {

    Observable<List<TestItem>> writeAllTestItem(List<TestItem> testItems);

    Observable<TestItem> getTestItem(String subj, String id, String ticket);

    Observable<TestItem> deleteTestItem(String subj, String id, String ticket);

    Observable<List<TestItem>> getAllTestItemsForTicket(String subj, String ticket);

    Observable<List<TestItem>> deleteAllTestItemsForTicket(String subj, String ticket);

    Observable<OcrResponseModel> getParsedTextFromFile(String mPath);

    Observable<TicketDataSet> getTicketDataSet(int ticket);

    Flowable writeTestItem(String id, boolean isQuestion, int currentTicket, int type, String value);
}
