package com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
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

    public Observable<OcrResponseModel> getParsedText(String base64, String lang)
    {

        String apikey = "9180640a9788957";
        Observable<OcrResponseModel> s = OcrService
                .getInstance()
                .getApi()
                .getData(apikey, "data:image/png;base64," + base64, lang, "true")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return s;
    }
}
