package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.SubjectTextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.TableDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IRootCreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestRootModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.Adapters.RootFragmentPagerAdapter;
import com.smallredtracktor.yourpersonaleducationalapplication.root.App;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CreateTestRootFragment extends Fragment
        implements IRootCreateTestFragmentMVPprovider.IFragment {

    private static final String OUTCOME_PARAM_ID = "outcome_id";
    private static final int MAX_OFFSCREEN_PAGE_LIMIT = 1000;

    @BindView(R.id.viewPager)
    ViewPager pager;
    @BindView(R.id.fabTabCreate)
    SpeedDialView fabTabCreate;
    Unbinder unbinder;
    RootFragmentPagerAdapter<CreateTestFragment> pagerAdapter;

    @Inject
    IRootCreateTestFragmentMVPprovider.IPresenter presenter;
    @Inject
    SubjectTextDialog textDialog;
    @Inject
    TableDialog tableDialog;
    private String outcome;


    public CreateTestRootFragment() {
    }

    public static CreateTestRootFragment newInstance(String outcome) {
        CreateTestRootFragment fragment = new CreateTestRootFragment();
        Bundle args = new Bundle();
        args.putString(OUTCOME_PARAM_ID, outcome);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            outcome = getArguments().getString(OUTCOME_PARAM_ID);
        }
    }

    @Override
    public void onResume() {
        presenter.setView(this);
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        App.get(Objects.requireNonNull(getContext()))
                .getComponent()
                .plusCreateTestRootComponent(new CreateTestRootModule(getContext()))
                .inject(this);
        View view = inflater.inflate(R.layout.fragment_create_test_root, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pagerAdapter = new RootFragmentPagerAdapter<>(getChildFragmentManager());
        fabTabCreate.getMainFab().setImageResource(R.drawable.ic_toolbox);
        fabTabCreate.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_table, R.drawable.ic_grid).create());
        fabTabCreate.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_create, R.drawable.ic_save).create());
        fabTabCreate.setOnActionSelectedListener(speedDialActionItem -> {
            switch (speedDialActionItem.getId()) {
                case R.id.fab_table: {
                    presenter.onTableFabClick();
                    break;
                }
                case R.id.fab_create: {
                    presenter.onSaveFabClick(pagerAdapter.getIdList(), outcome);
                    break;
                }
            }
            return false;
        });
        pager.setOffscreenPageLimit(MAX_OFFSCREEN_PAGE_LIMIT);
        pager.setPageMargin(50);
        presenter.onViewCreated();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                presenter.onPageSelected(i, pagerAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        pager.setAdapter(pagerAdapter);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter = null;
    }


    @Override
    public void showTable() {
        tableDialog.prepareTable(pagerAdapter.getItemList());
        tableDialog.show();
    }

    @Override
    public void hideTable() {
        tableDialog.dismiss();
    }

    @Override
    public void addFragmentToPageAdapter(int i, String id) {
        pagerAdapter.addFragment(CreateTestFragment.newInstance(String.valueOf(i + 1), id), i, id);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void setCurrentPage(int position) {
        pager.setCurrentItem(position);
    }

    @Override
    public void showTextDialog(String text, String id, List<String> strings) {
        textDialog.setDialogParams(text, id, strings);
        textDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void close() {
        getChildFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}