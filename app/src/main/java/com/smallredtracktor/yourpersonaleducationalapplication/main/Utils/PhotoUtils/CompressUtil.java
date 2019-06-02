package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CompressUtil {

    private final Context context;
    public CompressUtil(Context context) {
        this.context = context;
    }

    public Single<Bitmap> getBitmapSample(String content) {
        Single<Bitmap> s = Single.create(emitter -> {
            BitmapFactory.Options s2 = new BitmapFactory.Options();
            s2.inPreferredConfig = Bitmap.Config.RGB_565;
            s2.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(content, s2);
            s2.inSampleSize = calculateInSampleSize(s2, 100, 100);
            s2.inJustDecodeBounds = false;
            Bitmap s1 = BitmapFactory.decodeFile(content, s2);
            emitter.onSuccess(s1);
        });

        return s.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Single<Bitmap> getHalfSampleBitmap(String content) {
        Single<Bitmap> s = Single.create(emitter -> {
            BitmapFactory.Options s2 = new BitmapFactory.Options();
            s2.inPreferredConfig = Bitmap.Config.RGB_565;
            s2.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(content, s2);
            s2.inSampleSize = calculateInSampleSize(s2, 700, 700);
            s2.inJustDecodeBounds = false;
            Bitmap s1 = BitmapFactory.decodeFile(content, s2);
            emitter.onSuccess(s1);
        });

        return s.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Bitmap> getFullSizeBitmap(String content) {
        Single<Bitmap> s = Single.create(emitter -> {
            BitmapFactory.Options s2 = new BitmapFactory.Options();
            s2.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap s1 = BitmapFactory.decodeFile(content, s2);
            emitter.onSuccess(s1);
        });

        return s.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

