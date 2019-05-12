package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.SubjectTextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IRootCreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestRootModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.Adapters.RootFragmentPagerAdapter;
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
    @BindView(R.id.viewPager)
    ViewPager pager;
    Unbinder unbinder;
    RootFragmentPagerAdapter<CreateTestFragment> pagerAdapter;
    @BindView(R.id.fabTabSwitch)
    FloatingActionButton fabTabSwitch;
    @BindView(R.id.fabTabCreate)
    FloatingActionButton fabTabCreate;
    @BindView(R.id.createRootHost)
    CoordinatorLayout createRootHost;

    @Inject
    IRootCreateTestFragmentMVPprovider.IPresenter presenter;
    @Inject
    SubjectTextDialog textDialog;
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
        fabTabSwitch.setOnClickListener(v -> presenter.onTableFabClick());
        fabTabCreate.setOnClickListener(view1 -> presenter.onSaveFabClick(pagerAdapter.getIdList(), outcome));
        pager.setOffscreenPageLimit(100);
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
    public void hideTable() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.createRootHost, new Fragment())
                .commit();
        fabTabSwitch.setImageResource(R.drawable.ic_fab_table);
        fabTabSwitch.setVisibility(View.VISIBLE);
        pager.setVisibility(View.VISIBLE);
        pager.setClickable(true);
    }



    @Override
    public void showTable() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.createRootHost, TabCreateTestFragment.newInstance("", "",
                        pagerAdapter.getItemList(),
                        presenter))
                .commit();
        fabTabSwitch.setVisibility(View.INVISIBLE);
        pager.setVisibility(View.INVISIBLE);
        pager.setClickable(false);
    }

    @Override
    public void addFragmentToPageAdapter(int i) {
        String unique = UniqueDigit.getUnique();
        pagerAdapter.addFragment(CreateTestFragment.newInstance(String.valueOf(i + 1), unique), i, unique);
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
        getChildFragmentManager().beginTransaction()
                .remove(this)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}