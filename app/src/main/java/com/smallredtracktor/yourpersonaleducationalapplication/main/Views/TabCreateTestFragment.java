package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.Adapters.RootFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


@SuppressLint("ValidFragment")
public class TabCreateTestFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final RootFragmentPagerAdapter<CreateTestFragment> pagerAdapter;
    @BindView(R.id.tableLayout)
    TableLayout tableLayout;
    Unbinder unbinder;
    private String mParam1;
    private String mParam2;


    @SuppressLint("ValidFragment")
    public TabCreateTestFragment(RootFragmentPagerAdapter<CreateTestFragment> pagerAdapter) {
        this.pagerAdapter = pagerAdapter;
    }

    public static TabCreateTestFragment newInstance(String param1, String param2, RootFragmentPagerAdapter<CreateTestFragment> pagerAdapter) {
        TabCreateTestFragment fragment = new TabCreateTestFragment(pagerAdapter);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int BOOKSHELF_ROWS = 5;
        int BOOKSHELF_COLUMNS = 3;
        int g = 0;
        for (int i = 0; i < BOOKSHELF_ROWS; i++) {

            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < BOOKSHELF_COLUMNS; j++) {
                tableRow.addView(pagerAdapter.getItem(g).getView(), j);
                g++;
            }

            tableLayout.addView(tableRow, i);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
