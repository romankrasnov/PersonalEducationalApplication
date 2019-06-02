package com.smallredtracktor.yourpersonaleducationalapplication.main.Models;


import android.graphics.Bitmap;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;


public class CreateTestFragmentModel implements ICreateTestFragmentMVPprovider.IModel {

    private ICreateTestRepository repository;

    public CreateTestFragmentModel(ICreateTestRepository repository) {
        this.repository = repository;
    }


    @Override
    public Flowable<List<TestItem>> getTestItem(String id) {
        return repository.getTestItem(id);
    }

    @Override
    public void deleteTestItem(String id) {
        repository.deleteTestItem(id);
    }

    @Override
    public void deleteFile(String filepath)
    {
        repository.deleteFile(filepath);
    }


    @Override
    public Maybe<OcrResponseModel> getParsedTextResult(Bitmap bitmap) {
        return repository.getParsedTextFromBitmap(bitmap);
    }

    @Override
    public void updateTestItem(String id, boolean isQuestion, String currentTicket, int type, String value) {
        repository.writeTestItem(id,  isQuestion,  currentTicket, type,  value);
    }
}
