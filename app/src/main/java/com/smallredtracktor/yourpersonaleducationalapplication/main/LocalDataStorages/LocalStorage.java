package com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataStorages;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LocalStorage implements ILocalStorage {


    private final Context context;

    public LocalStorage(Context context) {
        this.context = context;
    }

    @Override
    public void deleteFile(String filename) {
        Single.create(emitter -> {
            File file = new File(filename);
            boolean delete = file.delete();
            Log.d("thread", Thread.currentThread().getName() + " delete file 1st " + delete);
            if(file.exists()){
                try {
                    boolean delete1 = file.getCanonicalFile().delete();
                    Log.d("thread", Thread.currentThread().getName() + " delete 2nd " + delete1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(file.exists()){
                    context.getApplicationContext().deleteFile(file.getName());
                    Log.d("thread", Thread.currentThread().getName() + " delete 3nd ");
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
