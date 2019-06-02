package com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OcrHelper {

    private static final String BASE64_IMAGE_PREFIX = "data:image/png;base64,";
    private static final String OCR_API_KEY = "9180640a9788957";
    private static final String OCR_DETECT_TEXT_ORIENTATION_FLAG = "true";

    private static OcrHelper mInstance;

    public static OcrHelper getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new OcrHelper();
        }
        return  mInstance;
    }

    public Maybe<OcrResponseModel> getParsedText(String base64, String lang)
    {
        return OcrService
                .getInstance()
                .getApi()
                .getData(OCR_API_KEY, BASE64_IMAGE_PREFIX + base64, lang, OCR_DETECT_TEXT_ORIENTATION_FLAG)
                .retry(20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
