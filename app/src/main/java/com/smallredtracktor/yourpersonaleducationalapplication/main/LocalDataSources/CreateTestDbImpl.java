package com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources;

import android.content.Context;
import android.os.HandlerThread;
import android.support.annotation.NonNull;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TicketDataSet;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class CreateTestDbImpl implements ICreateTestDbApi{


    private final Context context;

    private Realm realm;


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
        return realm.where(TestItem.class).findAll();
    }

    @Override
    public Flowable<List<TestItem>> itemById(String id) {
        RealmQuery<TestItem> query = realm.where(TestItem.class)
                .equalTo("id", id);
        Flowable<RealmResults<TestItem>> flowable;
        if(realm.isAutoRefresh()) { // for looper threads. Use `switchMap()`!
            flowable = query
                    .findAllAsync()
                    .asFlowable()
                    .filter(RealmResults::isLoaded);
        } else { // for background threads
            flowable = Flowable.just(query.findAllAsync());
        }

        // noinspection unchecked
        return (Flowable)flowable;
    }


    @Override
    public Observable<TicketDataSet> getTicketDataSet(int ticket) {
        return null;
    }

    @Override
    public Flowable<List<TestItem>> writeTestItem(String id, boolean isQuestion, int currentTicket, int type, String value) {
        TestItem item = new TestItem();
        item.setId(id);
        item.setQuestion(isQuestion);
        item.setTicketId(currentTicket);
        item.setType(type);
        item.setValue(value);

        realm.beginTransaction();
        realm.insert(item);
        realm.commitTransaction();

        // noinspection unchecked
        return itemById(item.getId());
    }
}
