package com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;


import java.util.List;
import io.reactivex.Flowable;


public interface ICreateTestDbApi {

    Flowable<List<TestItem>> itemById(String id);
    void updateTestItem(String id, boolean isQuestion, int currentTicket, int type, String value);
    void deleteTestItem(String id);
}
