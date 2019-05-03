package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import android.support.annotation.Nullable;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.POJOs.OcrResponseModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects.TestItem;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.TextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.STUB_PARAM_ID;

public class CreateTestFragmentPresenter implements
        ICreateTestFragmentMVPprovider.IPresenter,
        ChooseSourceDialog.ChooseSourceDialogListener,
        TextDialog.TextDialogListener {

    public static final String STATUS_LOADING = "loading";
    @Nullable
    private ICreateTestFragmentMVPprovider.IFragment view;
    @Nullable
    private ICreateTestFragmentMVPprovider.IModel model;
    private HashMap<String,DisposableObserver<List<TestItem>>> readSubscriberMap = new HashMap<>();
    private int currentTicket;


    public CreateTestFragmentPresenter(@Nullable ICreateTestFragmentMVPprovider.IModel model) {
        this.model = model;
    }


    @Override
    public void setView(@Nullable ICreateTestFragmentMVPprovider.IFragment view) {
        this.view = view;
    }

    @Override
    public void onAddQuestionClick() {
        if (view != null) {
            view.showChooseSourceDialog(true);
        }
    }

    @Override
    public void onQuestionPressed(String id) {
        if (view != null && model != null) {
            Observable<List<TestItem>> readTestItem = model
                    .getTestItem(id)
                    .toObservable();
            readSubscriberMap.put(id ,new DisposableObserver<List<TestItem>>() {
                @Override
                public void onNext(List<TestItem> testItems) {
                    if (testItems.size() != 0) {
                        try {
                            if(testItems.get(0).getType() == 0 || testItems.get(0).getType() ==3)
                            {
                                view.showTextFragment(testItems.get(0).getId(),
                                        testItems.get(0).getValue(),
                                        testItems.get(0).getType() ,
                                        testItems.get(0).isQuestion());
                            } else if(testItems.get(0).getType() == 1 || testItems.get(0).getType() ==2)
                            {
                                view.showPhotoFragment(testItems.get(0).getId(),
                                        testItems.get(0).getValue(),
                                        testItems.get(0).getType() ,
                                        testItems.get(0).isQuestion());
                            }
                        } catch (Exception e) {
                            view.showToast(testItems.get(0).getValue());
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    view.showToast(e.getMessage());
                }

                @Override
                public void onComplete() {
                }
            });
            readTestItem.subscribe(Objects.requireNonNull(readSubscriberMap.get(id)));
        }
    }


    @Override
    public void onGalleryResult(String path, int type, boolean isQuestion) {
        if (view != null && model != null) {
            String id = UniqueDigit.getUnique();
            if (isQuestion) {
                view.setTextQuestion(id, type, STATUS_LOADING);
            } else {
                view.setCurrentAnswer(id, type, null);
                view.addNewAnswer();
            }
            readSubscriberMap.put(id, new DisposableObserver<List<TestItem>>() {
                @Override
                public void onNext(List<TestItem> testItems) {
                    if (testItems.size() != 0) {
                        try {
                            if (isQuestion) {
                                if(testItems.get(0).getType() == 1 || testItems.get(0).getType() ==2)
                                {
                                    view.setPhotoQuestion(id, type, testItems.get(0).getValue());
                                }

                            } else {
                                view.setCurrentAnswer(id, type, testItems.get(0).getValue());
                            }
                        } catch (Exception e) {
                            view.showToast(testItems.get(0).getValue());
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    view.showToast(e.getMessage());
                }

                @Override
                public void onComplete() {
                }
            });
            Observable<List<TestItem>> readTestItem = null;

            if(type == 2)
            {
                // noinspection unchecked
                model.updateTestItem(id, isQuestion, currentTicket, type, path);
                readTestItem = model
                        .getTestItem(id)
                        .toObservable();
            }

            assert readTestItem != null;
            readTestItem
                    .subscribe(Objects.requireNonNull(readSubscriberMap.get(id)));
        }
    }

    @Override
    public void onBackPressed() {

    }


    @Override
    public void onAcceptSubject() {

    }


    @Override
    public void onPhotoTaken(String path, int type, boolean isQuestion) {
        if (view != null && model != null) {
            String id = UniqueDigit.getUnique();
            if (isQuestion) {
                    view.setTextQuestion(id, type, STATUS_LOADING);
            } else {
                view.setCurrentAnswer(id, type, null);
                    view.addNewAnswer();
            }
            readSubscriberMap.put(id ,new DisposableObserver<List<TestItem>>() {
                @Override
                public void onNext(List<TestItem> testItems) {
                    if (testItems.size() != 0) {
                        try {
                            if (isQuestion) {
                            if(testItems.get(0).getType() == 3) {
                                    view.setTextQuestion(testItems.get(0).getId(),
                                            testItems.get(0).getType(),
                                            testItems.get(0).getValue());
                                } else if(testItems.get(0).getType() != 0){
                                    view.setPhotoQuestion(testItems.get(0).getId(),
                                            testItems.get(0).getType(),
                                            testItems.get(0).getValue());
                            }

                            } else {
                                    view.setCurrentAnswer(testItems.get(0).getId(),
                                            testItems.get(0).getType(),
                                            testItems.get(0).getValue());
                                }
                        } catch (Exception e) {
                            view.showToast(testItems.get(0).getValue());
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    view.showToast(e.getMessage());
                }

                @Override
                public void onComplete() {
                }
            });
            Observable<List<TestItem>> readTestItem = null;
            switch (type) {
                case 3: {
                    // noinspection unchecked
                    readTestItem = model
                            .getParsedTextResult(path)
                            .flatMap((Function<OcrResponseModel, ObservableSource<List<TestItem>>>) ocrResponseModel -> {
                                model.updateTestItem(id, isQuestion, currentTicket, type, ocrResponseModel.getText());
                                return model.getTestItem(id).toObservable();
                            });
                    break;
                }
                case 1: {
                    // noinspection unchecked
                    model.updateTestItem(id, isQuestion, currentTicket, type, path);
                    readTestItem = model
                            .getTestItem(id)
                            .toObservable();
                    break;
                }
            }

            assert readTestItem != null;
            readTestItem
                    .subscribe(Objects.requireNonNull(readSubscriberMap.get(id)));
        }
    }


    @Override
    public void rxUnsubscribe() {
        for (DisposableObserver<List<TestItem>> listDisposableObserver : readSubscriberMap.values()) {
            listDisposableObserver.dispose();
        }
    }

    @Override
    public void onViewResumed(String s) {
        if (view != null) {
            view.setCounterTextView(s);
            currentTicket = Integer.parseInt(s);
        }
    }



    @Override
    public void onDialogTextSourceClick(boolean isQuestion) {
        if (view != null) {
            view.showTextFragment(UniqueDigit.getUnique(), "", 0, isQuestion);
        }
    }

    @Override
    public void onDialogPhotoSourceClick(boolean isQuestion) {
        if (view != null) {
            view.resolveCameraPermission();
            view.showCameraFragment(1, isQuestion);
        }
    }

    @Override
    public void onDialogGallerySourceClick(boolean isQuestion) {
        if (view != null) {
            view.showGallery(2, isQuestion);
        }
    }

    @Override
    public void onDialogOcrSourceClick(boolean isQuestion) {
        if (view != null) {
            view.resolveCameraPermission();
            view.showCameraFragment(3, isQuestion);
        }
    }

    @Override
    public void onTextDialogOkClick(String id, String content, int type, boolean isQuestion) {
        if (model != null && view != null) {
            if(readSubscriberMap.containsKey(id))
            {
                Objects.requireNonNull(readSubscriberMap.get(id)).dispose();
            }
            model.updateTestItem(id, isQuestion, currentTicket, type, content);
            if (isQuestion) {
                view.setTextQuestion(id, type, content);
            } else {
                view.setCurrentAnswer(id, type, content);
                view.addNewAnswer();
            }
        }
    }

    @Override
    public void onTextDialogCancelClick() {

    }

    @Override
    public void onAnswerFragmentInteraction(String id) {
        if (view != null && model != null) {
            if (id.equals(STUB_PARAM_ID)) {
                view.showChooseSourceDialog(false);
            } else {
                Observable<List<TestItem>> readTestItem = model
                        .getTestItem(id)
                        .toObservable();
                readSubscriberMap.put(id, new DisposableObserver<List<TestItem>>() {
                    @Override
                    public void onNext(List<TestItem> testItems) {
                        if (testItems.size() != 0) {
                            try {
                                if(testItems.get(0).getType() == 0 || testItems.get(0).getType() ==3)
                                {
                                    view.showTextFragment(testItems.get(0).getId(),
                                            testItems.get(0).getValue(),
                                            testItems.get(0).getType() ,
                                            testItems.get(0).isQuestion());
                                } else if(testItems.get(0).getType() == 1 || testItems.get(0).getType() ==2)
                                {
                                    view.showPhotoFragment(testItems.get(0).getId(),
                                            testItems.get(0).getValue(),
                                            testItems.get(0).getType() ,
                                            testItems.get(0).isQuestion());
                                }
                            } catch (Exception e) {
                                view.showToast(testItems.get(0).getValue());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                readTestItem.subscribe(Objects.requireNonNull(readSubscriberMap.get(id)));
            }
        }
    }

    @Override
    public void onAnswerLongClick(String id) {
        if (view != null && model != null) {
            if(!id.equals(STUB_PARAM_ID))
            {
                view.removeAnswer(id);
                if(readSubscriberMap.containsKey(id))
                {
                    Objects.requireNonNull(readSubscriberMap.get(id)).dispose();
                }
                model.deleteTestItem(id);
            }
        }
    }

    @Override
    public boolean onQuestionLongPressed(String id) {
        if (view != null && model != null) {
            if(!id.equals(STUB_PARAM_ID))
            {
                view.deleteQuestion();
                if(readSubscriberMap.containsKey(id))
                {
                    Objects.requireNonNull(readSubscriberMap.get(id)).dispose();
                }
                model.deleteTestItem(id);
            }
        }
        return false;
    }
}


