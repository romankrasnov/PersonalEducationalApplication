package com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources;



import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;

import java.util.List;

import io.reactivex.Observable;

public interface ICreateTestDbApi {

    Observable<List<TestItem>> itemEntityList();
    Observable<TestItem> itemById(String id);
    Observable<TestItem> writeTestItem(TestItem testItem);
    Observable<TicketDataSet> getTicketDataSet(int ticket);
}
