package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;

@SuppressLint("ValidFragment")
public class TextDialog extends DialogFragment {

    private int type;
    private boolean isQuestion;
    private String id;
    private String text;

    @SuppressLint("ValidFragment")
    public TextDialog(ICreateTestFragmentMVPprovider.IPresenter presenter) {
        this.createTestFragmentPresenter = presenter;
    }

    public interface TextDialogListener {
        void onTextDialogOkClick(String id, String text, int type, boolean isQuestion);
        void onTextDialogCancelClick();
    }

    TextDialogListener listener;
    ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (TextDialogListener) createTestFragmentPresenter;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        EditText ed = new EditText(getContext());
        ed.setVerticalScrollBarEnabled(true);
        ed.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ed.setText(text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit")
                .setPositiveButton("Ok", (dialog, which) -> listener.onTextDialogOkClick(id, ed.getText().toString(), type, isQuestion))
                .setNegativeButton("Cancel", (dialog, which) -> listener.onTextDialogCancelClick())
                .setView(ed);
        return builder.create();
    }

    public void setDialogParams(String text, String id, int type, boolean isQuestion)
    {
        this.id = id;
        this.type = type;
        this.isQuestion = isQuestion;
        this.text = text;
    }
}
