package com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OcrHelper {

    private static OcrHelper mInstance;
    private String result;

    public static OcrHelper getIntance()
    {
        if (mInstance == null)
        {
            return new OcrHelper();
        }
        return  mInstance;
    }

    public String getParsedText(String base64, String lang)
    {
        String apikey = "9180640a9788957";
        OcrService.getInstance()
                .getApi()
                .getData(apikey,"data:image/png;base64," + base64,lang)
                .enqueue(new Callback<OcrResponseModel>() {
                    @Override
                    public void onResponse(Call<OcrResponseModel> call, Response<OcrResponseModel> response) {

                        if (response.body() != null) {
                            //lets make it observable
                            result = response.body().getParsedResults().get(0).getParsedText().toString();
                        }
                    }

                    @Override
                    public void onFailure(Call<OcrResponseModel> call, Throwable t) {
                        result = t.getMessage();
                    }
                });
        return result;
    }
}
