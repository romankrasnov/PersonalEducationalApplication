package com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise;


import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OcrHelper {

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
        String apikey = "9180640a9788957";
        return OcrService
                .getInstance()
                .getApi()
                .getData(apikey, "data:image/png;base64," + base64, lang, "true")
                .retry(20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
