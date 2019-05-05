package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise.OcrHelper;
import java.io.ByteArrayOutputStream;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ParseTextUtil {

    private static final String OCR_LANGUAGE = "rus";

    public ParseTextUtil() {}

    public  Single<OcrResponseModel> getResult(String mPath)
    {
        Single<Bitmap> s = getBitmap(mPath);
        return s.flatMap((Function<Bitmap, Single<String>>)
                this::calculateBase64)
                .flatMap((Function<String, Single<OcrResponseModel>>)
                        value -> OcrHelper.getInstance().getParsedText(value, OCR_LANGUAGE));
    }

    private Single<String> calculateBase64(Bitmap bitmap)
    {
        Single<String> s = Single.create(emitter -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            emitter.onSuccess(base64);
        });
        return s.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<Bitmap> getBitmap(String path)
    {
        Single<Bitmap> s = Single.create(emitter -> {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            emitter.onSuccess(bitmap);
        });
        return s.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
