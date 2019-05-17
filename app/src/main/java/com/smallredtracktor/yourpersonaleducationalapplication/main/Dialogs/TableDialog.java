package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IRootCreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment;

import java.util.List;
import java.util.Objects;


import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.OUTCOME_PARAM_COUNT;
import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.STUB_PARAM_ID;

public class TableDialog extends Dialog {

    private final int columnCount = 4;
    private TableDialogListener listener;
    private List<CreateTestFragment> pagerList;
    private final int height = (Resources.getSystem().getDisplayMetrics().heightPixels) / 8;
    private final int width = Resources.getSystem().getDisplayMetrics().widthPixels / columnCount;
    private TableLayout tableLayout;

    public TableDialog(@NonNull Context context, IRootCreateTestFragmentMVPprovider.IPresenter presenter) {
        super(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setContentView(R.layout.fragment_tab_create_test);
        tableLayout = findViewById(R.id.tableLayout);
        if(presenter instanceof TableDialogListener)
        {
            listener = (TableDialogListener) presenter;
        }
    }

    public interface TableDialogListener {
        void onTableItemInteraction(int position);
    }

    private String getListIndex(int i) {
        return Objects.requireNonNull(pagerList.get(i).getArguments()).getString(OUTCOME_PARAM_COUNT);
    }

    private Button getPreparedButton(int i, String text) {
        Button item = new Button(getContext());
        item.setBackgroundColor(0);
        item.setWidth(width);
        item.setHeight(height);
        item.setText(text);
        item.setOnClickListener(view1 -> listener.onTableItemInteraction(i));
        return item;
    }

    private TableRow getPreparedTableRow() {
        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return tableRow;
    }

    @Override
    public void dismiss() {
        tableLayout.removeAllViews();
        super.dismiss();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tableLayout.removeAllViews();
    }

    public void prepareTable(List<CreateTestFragment> pagerList) {
        this.pagerList = pagerList;
        int pageCount = pagerList.size();
        if (pageCount < 4) {
            TableRow row = getPreparedTableRow();
            for (int i = 0; i < pageCount; i++) {
                String text;
                if (i == pageCount - 1) {
                    text = STUB_PARAM_ID;
                } else {
                    text = getListIndex(i);
                }
                Button btn = getPreparedButton(i, text);
                row.addView(btn, i);
            }
            tableLayout.addView(row, 0);

        } else {
            int itemCounter = 0;
            int rowCounter = 0;
            int rowCount = pageCount / columnCount;
            int additionalItems = pageCount % columnCount;
            for (int i = 0; i < rowCount; i++) {
                TableRow row = getPreparedTableRow();
                for (int j = 0; j < columnCount; j++) {
                    String text;
                    if (itemCounter == pageCount - 1) {
                        text = STUB_PARAM_ID;
                    } else {
                        text = getListIndex(itemCounter);
                    }
                    Button btn = getPreparedButton(itemCounter, text);
                    row.addView(btn, j);
                    itemCounter++;
                }
                rowCounter++;
                tableLayout.addView(row, i);
            }

            if (additionalItems != 0) {
                TableRow row = getPreparedTableRow();
                for (int i = 0; i < additionalItems; i++) {
                    String text;
                    if (i + itemCounter == pageCount - 1) {
                        text = STUB_PARAM_ID;
                    } else {
                        text = getListIndex(i + itemCounter);
                    }
                    Button btn = getPreparedButton(i + itemCounter, text);
                    row.addView(btn, i);
                }
                tableLayout.addView(row, rowCounter);
            }
        }
    }
}
