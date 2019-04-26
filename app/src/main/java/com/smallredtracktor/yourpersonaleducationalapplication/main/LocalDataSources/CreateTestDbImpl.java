package com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources;

import android.content.Context;
import android.support.annotation.NonNull;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class CreateTestDbImpl implements ICreateTestDbApi{


    public CreateTestDbImpl(@NonNull Context context) {
        Realm.init(context);
    }

    @Override
    public Observable<TicketDataSet> getTicketDataSet(int ticket) {
        return null;
    }

    @Override
    public Observable<List<TestItem>> itemEntityList() {
        return null;
    }

    @Override
    public Flowable<List<TestItem>> itemById(String id) {
        Realm realm = Realm.getDefaultInstance();
            RealmQuery<TestItem> query = realm.where(TestItem.class)
                    .equalTo("id", id);
            Flowable<RealmResults<TestItem>> flowable;
            if (realm.isAutoRefresh()) {
                flowable = query
                        .findAllAsync()
                        .asFlowable()
                        .filter(RealmResults::isLoaded);
            } else {
                flowable = Flowable.just(query.findAllAsync());
            }
            // noinspection unchecked
            return (Flowable) flowable;
    }

    @Override
    public void writeTestItem(String id, boolean isQuestion, int currentTicket, int type, String value) {
        new Thread(() -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realm1 -> {
                    TestItem item = new TestItem();
                    item.setId(id);
                    item.setQuestion(isQuestion);
                    item.setTicketId(currentTicket);
                    item.setType(type);
                    item.setValue(value);
                    realm1.insert(item);
                });
            }
        }).start();
    }

}