package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class TextDialog extends DialogFragment {

    private String text;

    @SuppressLint("ValidFragment")
    public TextDialog(ICreateTestFragmentMVPprovider.IPresenter presenter) {
        this.createTestFragmentPresenter = presenter;
    }

    public interface TextDialogListener {
        void onTextDialogOkClick(String string);
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
                .setPositiveButton("Ok", (dialog, which) -> listener.onTextDialogOkClick(ed.getText().toString()))
                .setNegativeButton("Cancel", (dialog, which) -> listener.onTextDialogCancelClick())
                .setView(ed);
        return builder.create();
    }

    public void setDialogText(String text)
    {
        this.text = text;
    }
}
