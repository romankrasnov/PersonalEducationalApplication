package com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources;

import android.content.Context;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Test;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.Ticket;

import java.util.List;

import io.realm.Realm;

public class CreateTestRootDbImpl implements ICreateTestRootDbApi {

    public CreateTestRootDbImpl(Context context) {
        Realm.init(context);
    }

    @Override
    public void writeSubject(String id, String name) {
        new Thread(() -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realm1 -> {
                    Test item = new Test();
                    item.setId(id);
                    item.setName(name);
                    realm1.insertOrUpdate(item);
                });
            }
        }).start();
    }

    @Override
    public void writeTickets(List<String> strings, String id) {
        new Thread(() -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realm1 -> {
                    for (int i = 0; i<strings.size(); i++) {
                        Ticket item = new Ticket();
                        item.setId(strings.get(i));
                        item.setSubjectId(id);
                        realm1.insertOrUpdate(item);
                    }
                });
            }
        }).start();
    }
}
