package com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources;



import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface ICreateTestDbApi {

    Observable<List<TestItem>> itemEntityList();
    Flowable<List<TestItem>> itemById(String id);
    Observable<TicketDataSet> getTicketDataSet(int ticket);
    Flowable writeTestItem(String id, boolean isQuestion, int currentTicket, int type, String value);
}
