package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;

import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;



@SuppressLint("ValidFragment")
public class ChooseSourceDialog extends DialogFragment {


    private boolean isQuestion;
    private ChooseSourceDialogListener listener;
    private ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter;
    @SuppressLint("ValidFragment")
    public ChooseSourceDialog(ICreateTestFragmentMVPprovider.IPresenter presenter) {
        this.createTestFragmentPresenter = presenter;

    }

    public void setIsQuestion(boolean isQuestion) {
        this.isQuestion = isQuestion;
    }

    public interface ChooseSourceDialogListener {
        void onDialogTextSourceClick(boolean isQuestion);
        void onDialogPhotoSourceClick(boolean isQuestion);
        void onDialogGallerySourceClick(boolean isQuestion);
        void onDialogOcrSourceClick(boolean isQuestion);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ChooseSourceDialogListener) createTestFragmentPresenter;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose source")
                .setItems(new String[] {"Text","Photo","Gallery", "OCR"}, (dialog, which) -> {
                    switch (which)
                    {
                        case 0:
                        {
                            listener.onDialogTextSourceClick(isQuestion);
                            break;
                        }
                        case 1:
                        {
                            listener.onDialogPhotoSourceClick(isQuestion);
                            break;
                        }
                        case 2:
                        {
                            listener.onDialogGallerySourceClick(isQuestion);
                            break;
                        }
                        case 3:
                        {
                            listener.onDialogOcrSourceClick(isQuestion);
                            break;
                        }
                    }
                });

        return builder.create();
    }

}

