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


import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IRootCreateTestFragmentMVPprovider;

import java.util.List;

@SuppressLint("ValidFragment")
public class SubjectTextDialog extends DialogFragment {

    private String text;
    private String id;
    TextDialogListener listener;
    IRootCreateTestFragmentMVPprovider.IPresenter presenter;
    private List<String> strings;

    @SuppressLint("ValidFragment")
    public SubjectTextDialog(IRootCreateTestFragmentMVPprovider.IPresenter presenter) {
        this.presenter = presenter;
    }

    public interface TextDialogListener {
        void onTextDialogCreate(String id);
        void onTextDialogInteraction(String id, String text, List<String> strings);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (TextDialogListener) presenter;
        } catch (ClassCastException e) {
            throw new ClassCastException(presenter.getClass().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        EditText ed = new EditText(getContext());
        ed.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ed.setText(text);
        listener.onTextDialogCreate(id);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Subject name")
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    text = ed.getText().toString();
                    listener.onTextDialogInteraction(id, text, strings);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) ->
                        listener.onTextDialogInteraction(id, null, strings))
                .setView(ed);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return builder.create();
    }

    public void setDialogParams(String text, String id, List<String> strings)
    {
        this.id = id;
        this.text = text;
        this.strings = strings;
    }

}
