package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CompressUtil {

    private final Context context;

    public CompressUtil(Context context) {
        this.context = context;
    }

    public Single<Bitmap> getBitmap(String content) {
        Single<Bitmap> s = Single.create(emitter -> {
            File file = new File(content);
            Bitmap s1 = null;
            try {
                Log.d("thread", Thread.currentThread().getName() + "COMPRESS UTIL");
                s1 = new Compressor(Objects.requireNonNull(context))
                        .setQuality(70)
                        .compressToBitmap(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            emitter.onSuccess(s1);
        });

        return s.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

