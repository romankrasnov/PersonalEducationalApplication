package com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces;


import android.graphics.Bitmap;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface ICreateTestRepository  {

    Flowable<List<TestItem>> getTestItem(String id);
    void deleteTestItem(String id);
    void deleteFile(String filepath);
    void writeTestItem(String id, boolean isQuestion, String currentTicket, int type, String value);
    Maybe<OcrResponseModel> getParsedTextFromBitmap(Bitmap bitmap);
}
