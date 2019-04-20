package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import android.support.annotation.Nullable;

import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.TextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;



import java.util.List;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;



import static android.app.Activity.RESULT_OK;

public class CreateTestFragmentPresenter implements
        ICreateTestFragmentMVPprovider.IPresenter,
      ChooseSourceDialog.ChooseSourceDialogListener,
        TextDialog.TextDialogListener {

    @Nullable
    private ICreateTestFragmentMVPprovider.IFragment view;
    @Nullable
    private ICreateTestFragmentMVPprovider.IModel model;

    private DisposableObserver<List<TestItem>> readSubscriber;
    private boolean isQuestion;
    private boolean isOcr;
    private int type;
    private int currentTicket = 1;


    public CreateTestFragmentPresenter(@Nullable ICreateTestFragmentMVPprovider.IModel model) {
        this.model = model;
    }


    @Override
    public void setView(@Nullable ICreateTestFragmentMVPprovider.IFragment view) {
        this.view = view;
    }


    @Override
    public void onSwipeTop() {

    }

    @Override
    public void onSwipeRight() {
        if (view != null) {
            if (currentTicket > 1) {
                currentTicket--;
                view.setCounterTextView(String.valueOf(currentTicket));
            }
        }
    }

    @Override
    public void onSwipeLeft() {
        if (view != null) {
            currentTicket++;
            view.setCounterTextView(String.valueOf(currentTicket));
        }
    }

    @Override
    public void onSwipeBottom() {
    }

    @Override
    public void onAddQuestionClick() {
        if (view != null) {
            isQuestion = true;
            view.showChooseSourceDialog();
        }
    }

    @Override
    public void onAddAnswerClick() {
        if (view != null) {
            isQuestion = false;
            view.showChooseSourceDialog();
        }
    }

    @Override
    public void onClearClick() {

    }

    @Override
    public void onDoneClick() {

    }


    @Override
    public void onGalleryResult() {

    }

    @Override
    public void onBackPressed() {

    }


    @Override
    public void onSubjectConfrimed() {

    }

    @Override
    public void onSubjectEdited() {

    }

    @Override
    public void onVideoRecorded() {

    }

    @Override
    public void onPhotoTaken(String mPath) {
        if (view != null && model != null) {
            String id = UniqueDigit.getUnique();
            if(isQuestion) {
                view.addSubjectToQuestionStack(id);
            }else {
                view.addSubjectToAnswerStack(id);
                }
            readSubscriber = new DisposableObserver<List<TestItem>>() {
                    @Override
                    public void onNext(List<TestItem> testItems) {
                        if (testItems.size() != 0) {
                            try {
                                view.showTextFragment(testItems.get(0).getValue());
                            } catch (Exception e) {
                                view.showToast(e.toString());
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showToast("Error while networking");
                    }

                    @Override
                    public void onComplete() {
                    }
                };
            Observable<List<TestItem>> readTestItem;
            if(isOcr)
            {                                                                                                                                                                                                                                                                                                         
                 // noinspection unchecked
                readTestItem = model
                        .getParsedTextResult(mPath)
                        .flatMap((Function<OcrResponseModel, ObservableSource<List<TestItem>>>) ocrResponseModel -> {
                            model.writeTestItem(id,isQuestion,currentTicket,type, ocrResponseModel.getText());
                            return model.getTestItem(id).toObservable();
                        });
            }
            else {
                // noinspection unchecked
                model.writeTestItem(id,isQuestion,currentTicket,type, mPath);
                readTestItem = model
                        .getTestItem(id)
                        .toObservable();
            }
                readTestItem
                        .subscribe(readSubscriber);
        }
    }

    @Override
    public boolean onPhotoPermissionCompatResult(int reqCode, int resCode) {
        switch (reqCode) {
            case 200:
                if (resCode == RESULT_OK) {
                    return true;
                }
        }
        return false;
    }

    @Override
    public void rxUnsubscribe()
    {   if (readSubscriber != null) {
        if (!readSubscriber.isDisposed()) {
            readSubscriber.dispose();
        }
    }
}

    @Override
    public void onObjectLongPressed(String id) {
        if (view != null) {
            view.setCounterTextView(id);
        }
    }

    @Override
    public void onPhotoTakingCancelled() {

    }

    @Override
    public void onDialogTextSourceClick() {
        if (view != null) {
            type = 0;
            view.showTextFragment("Если в диалоговом окне необходим пользовательский макет, нужно создать макет и добавить его в AlertDialog путем вызова setView() в объекте AlertDialog.Builder.\n" +
                    "\n" +
                    "По умолчанию пользовательский мает заполняет окно диалога, при это все равно можно использовать методы AlertDialog.Builder для добавления кнопок и заголовка.\n" +
                    "\n" +
                    "В качестве примера на рисунке 5 приведен файл макета для диалогового окна.");
        }
    }

    @Override
    public void onDialogPhotoSourceClick() {
        if (view != null) {
            isOcr = false;
            type = 1;
            view.resolveCameraPermission();
            view.showCameraFragment();
        }
    }

    @Override
    public void onDialogGallerySourceClick() {
        if (view != null) {
            view.showGallery();
            type = 2;
        }
    }

    @Override
    public void onDialogOcrSourceClick() {
        if (view != null) {
            isOcr = true;
            type = 3;
            view.resolveCameraPermission();
            view.showCameraFragment();
        }
    }

    @Override
    public void onTextDialogOkClick(String string) {

    }

    @Override
    public void onTextDialogCancelClick() {

    }
}
