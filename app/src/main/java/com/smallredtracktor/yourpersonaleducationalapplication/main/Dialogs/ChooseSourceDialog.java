package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;


import com.smallredtracktor.yourpersonaleducationalapplication.R;



public class ChooseSourceDialog extends DialogFragment {


    public interface ChooseSourceDialogListener {
        void onDialogTextSourceClick();
        void onDialogPhotoSourceClick();
        void onDialogGallerySourceClick();
        void onDialogOcrSourceClick();
    }

    // Use this instance of the interface to deliver action events
    ChooseSourceDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (ChooseSourceDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] chooseSourceItems = getResources().getStringArray(R.array.chooseSourceItems);

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.dialog_list_view, chooseSourceItems);
        builder.setMessage("Choose source")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
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

