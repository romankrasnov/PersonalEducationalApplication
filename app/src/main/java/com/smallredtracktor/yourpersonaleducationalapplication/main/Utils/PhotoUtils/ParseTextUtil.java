package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise.OcrHelper;
import java.io.ByteArrayOutputStream;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ParseTextUtil {

    private static final String OCR_LANGUAGE = "rus";
    private static final int OCR_PHOTO_QUALITY = 30;

    public ParseTextUtil() {}

    public  Maybe<OcrResponseModel> getResult(String mPath)
    {
        Maybe<Bitmap> s = getBitmap(mPath);
        return s.flatMap((Function<Bitmap, Maybe<String>>)
                this::calculateBase64)
                .flatMap((Function<String, Maybe<OcrResponseModel>>)
                        value -> OcrHelper.getInstance().getParsedText(value, OCR_LANGUAGE));
    }

    private Maybe<String> calculateBase64(Bitmap bitmap)
    {
        Maybe<String> s = Maybe.create(emitter -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, OCR_PHOTO_QUALITY, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            emitter.onSuccess(base64);
        });
        return s.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Maybe<Bitmap> getBitmap(String path)
    {
        Maybe<Bitmap> s = Maybe.create(emitter -> {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            emitter.onSuccess(bitmap);
        });
        return s.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
