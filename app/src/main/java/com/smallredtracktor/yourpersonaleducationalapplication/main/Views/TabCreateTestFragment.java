package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IRootCreateTestFragmentMVPprovider;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.OUTCOME_PARAM_COUNT;


@SuppressLint("ValidFragment")
public class TabCreateTestFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final List<CreateTestFragment> pagerList;
    @BindView(R.id.tableLayout)
    TableLayout tableLayout;
    Unbinder unbinder;
    private String mParam1;
    private String mParam2;

    private TabCreateTestFragmentListener listener;
    private int height;
    private int width;

    public interface TabCreateTestFragmentListener
    {
        void onTableItemInteraction(int position);
    }

    @SuppressLint("ValidFragment")
    public TabCreateTestFragment(List<CreateTestFragment> pagerList, TabCreateTestFragmentListener listener) {
        this.pagerList = pagerList;
        this.listener = listener;
    }

    public static TabCreateTestFragment newInstance(String param1, String param2, List<CreateTestFragment> pagerList, IRootCreateTestFragmentMVPprovider.IPresenter presenter) {
        TabCreateTestFragment fragment = new TabCreateTestFragment(pagerList, (TabCreateTestFragmentListener) presenter);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_create_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        int columnCount = 4;
        if (Objects.requireNonNull(getActivity()).getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        height = (Resources.getSystem().getDisplayMetrics().heightPixels  - actionBarHeight) / 8;
        width = Resources.getSystem().getDisplayMetrics().widthPixels  / columnCount;
        int pageCount = pagerList.size();
        if (pageCount < 4)
        {
            TableRow row = getPreparedTableRow();
            for (int i = 0; i < pageCount; i++)
            {
                String text;
                if(i == pageCount - 1)
                {
                    text = "new";
                } else
                    {
                        text = getListIndex(i);
                    }
                Button btn = getPreparedButton(i, text);
                row.addView(btn, i);
            }
            tableLayout.addView(row, 0);

        } else
            {
                int itemCounter = 0;
                int rowCounter = 0;
                int rowCount = pageCount / columnCount;
                int additionalItems = pageCount % columnCount;
                for (int i = 0; i < rowCount; i++) {
                    TableRow row = getPreparedTableRow();
                    for (int j = 0; j < columnCount; j++) {
                        String text;
                        if(itemCounter == pageCount - 1)
                        {
                            text = "new";
                        } else
                            {
                            text = getListIndex(itemCounter);
                            }
                        Button btn = getPreparedButton(itemCounter, text);
                        row.addView(btn, j);
                        itemCounter++;
                    }
                    rowCounter++;
                    tableLayout.addView(row, i);
                }

                if (additionalItems != 0)
                {
                    TableRow row = getPreparedTableRow();
                    for (int i = 0; i < additionalItems; i++)
                    {
                        String text;
                        if(i + itemCounter == pageCount - 1)
                        {
                            text = "new";
                        } else
                            {
                            text = getListIndex(i + itemCounter);
                            }
                        Button btn = getPreparedButton(i + itemCounter , text);
                        row.addView(btn, i);
                    }
                    tableLayout.addView(row, rowCounter);
                }
            }
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
