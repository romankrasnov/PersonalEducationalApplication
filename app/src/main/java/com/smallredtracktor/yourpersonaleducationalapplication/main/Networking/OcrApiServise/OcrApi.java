package com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OcrApi {

    @FormUrlEncoded
    @POST("parse/image")
    Maybe<OcrResponseModel> getData(
            @Field("apikey") String apikey,
            @Field("base64Image") String base64Image,
            @Field("language") String language,
            @Field("detectOrientation") String detectOrientation
    );
}
