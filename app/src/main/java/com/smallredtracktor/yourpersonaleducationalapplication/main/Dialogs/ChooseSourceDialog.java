package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;

import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.root.App;

import javax.inject.Inject;


public class ChooseSourceDialog extends DialogFragment {


    public interface ChooseSourceDialogListener {
        void onDialogTextSourceClick();
        void onDialogPhotoSourceClick();
        void onDialogGallerySourceClick();
        void onDialogOcrSourceClick();
    }


    ChooseSourceDialogListener listener;

    @Inject
    ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        App.get(context)
                .plusCreateTestComponent()
                .inject(this);
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
                .setItems(new String[] {"Text","Photo","Gallery", "OCR"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                            {
                                listener.onDialogTextSourceClick();
                                break;
                            }
                            case 1:
                            {
                                listener.onDialogPhotoSourceClick();
                                break;
                            }
                            case 2:
                            {
                                listener.onDialogGallerySourceClick();
                                break;
                            }
                            case 3:
                            {
                                listener.onDialogOcrSourceClick();
                                break;
                            }
                        }
                    }
                });

        return builder.create();
    }

}

