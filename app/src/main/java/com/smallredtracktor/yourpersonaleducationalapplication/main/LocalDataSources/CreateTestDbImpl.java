package com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources;

import android.content.Context;
import android.support.annotation.NonNull;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;



import java.util.List;
import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class CreateTestDbImpl implements ICreateTestDbApi{


    public CreateTestDbImpl(@NonNull Context context) {
        Realm.init(context);
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
    public void updateTestItem(String id, boolean isQuestion, String currentTicket, int type, String value) {
        Single.create(emitter -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realm1 -> {
                    TestItem item = new TestItem();
                    item.setId(id);
                    item.setQuestion(isQuestion);
                    item.setTicketId(currentTicket);
                    item.setType(type);
                    item.setValue(value);
                    realm1.insertOrUpdate(item);
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void deleteTestItem(String id) {
        Single.create(emitter -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realm1 -> {
                    RealmQuery<TestItem> query = realm
                            .where(TestItem.class)
                            .equalTo("id", id);
                    Objects.requireNonNull(query.findAll()).deleteLastFromRealm();
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
