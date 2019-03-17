package com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OcrService {

    private static final String BASE_URL = "https://api.ocr.space/";
    private static OcrService mInstance;
    private Retrofit mRetrofit;

    private OcrService ()
    {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static OcrService getInstance() {
        if (mInstance == null) {
            mInstance = new OcrService();
        }
        return mInstance;
    }

    OcrApi getApi()
        {
         return mRetrofit.create(OcrApi.class);
        }
}


