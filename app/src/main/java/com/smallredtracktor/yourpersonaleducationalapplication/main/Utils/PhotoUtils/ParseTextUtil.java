package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise.OcrHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ParseTextUtil {

    public ParseTextUtil() {}

    public  Observable<OcrResponseModel> getResult(String mPath)
    {
        Observable<Bitmap> s = getBitmap(mPath);
        return s.flatMap((Function<Bitmap, ObservableSource<String>>)
                this::calculateBase64)
                .flatMap((Function<String, ObservableSource<OcrResponseModel>>)
                        value -> OcrHelper.getIntance().getParsedText(value, "rus"));
    }

    private Observable<String> calculateBase64(Bitmap bitmap)
    {
        Observable<String> s = Observable.create(emitter -> {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            emitter.onNext(base64);
        });
        return s.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Bitmap> getBitmap(String path)
    {
        Observable<Bitmap> s = Observable.create(emitter -> {
            File imgFile = new File(path);
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            emitter.onNext(bitmap);
        });
        return s.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
