package com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OcrApi {

    @FormUrlEncoded
    @POST("parse/image")
    Call<OcrResponseModel> getData(
            @Field("apikey") String apikey,
            @Field("base64Image") String base64Image,
            @Field("language") String language
    );
}
