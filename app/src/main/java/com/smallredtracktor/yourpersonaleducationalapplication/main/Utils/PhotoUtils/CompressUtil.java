package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CompressUtil {

    static final int LOCAL_PHOTO_QUALITY = 100;
    private final Context context;
    public CompressUtil(Context context) {
        this.context = context;
    }

    public Single<Bitmap> getBitmap(String content) {
        Single<Bitmap> s = Single.create(emitter -> {
            BitmapFactory.Options s2 = new BitmapFactory.Options();
            s2.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap s1 = BitmapFactory.decodeFile(content, s2);
            emitter.onSuccess(s1);
        });

        return s.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

