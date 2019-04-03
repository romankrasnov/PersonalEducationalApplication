package com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources;

import android.content.Context;
import android.support.annotation.NonNull;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;


public class CreateTestDbImpl implements ICreateTestDbApi{


    private final Context context;
    private Realm realm;

    @Inject
    public CreateTestDbImpl(@NonNull Context context) {
        this.context = context;
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }


    @Override
    public Observable<List<TestItem>> itemEntityList() {
        return Observable.create(emitter -> {
            List<TestItem> ticketItemList = get();
            if (ticketItemList != null) {
                emitter.onNext(ticketItemList);
                emitter.onComplete();
            } else {
                emitter.onError(
                        new Throwable("Error getting main data list from the local db"));
            }
        });
    }

    private List<TestItem> get() {
        realm.beginTransaction();

        for (int i =0; i<3000; i++) {
            TestItem item = realm.createObject(TestItem.class);
            item.setId(String.valueOf(Math.random()));
            item.setTicketId(Integer.parseInt(String.valueOf(Math.random())));
            item.setType(Integer.parseInt(String.valueOf(Math.random())));
            item.setValue(String.valueOf(Math.random()));
        }
        realm.commitTransaction();
        return realm.where(TestItem.class).findAll();
    }

    @Override
    public Observable<TestItem> itemById(String id) {
        return null;
    }

    @Override
    public Observable<TestItem> writeTestItem(TestItem testItem) {

        Observable<TestItem> observable = Observable
                .create(emitter -> {
            TestItem item = writeItem(testItem);
            if (item != null) {
                emitter.onNext(item);
                emitter.onComplete();
            } else {
                emitter.onError(
                        new Throwable("Error getting main data list from the local db"));
            }
        });

        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private TestItem writeItem(TestItem testItem) {
        realm.beginTransaction();
        realm.insert(testItem);
        realm.commitTransaction();
        return realm.where(TestItem.class).contains("id", testItem.getId()).findFirst();
    }

    @Override
    public Observable<TicketDataSet> getTicketDataSet(int ticket) {
        return null;
    }
}
