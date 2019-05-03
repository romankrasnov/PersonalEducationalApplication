package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.Adapters.RootFragmentPagerAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateTestRootFragment extends Fragment {


    @BindView(R.id.fabCreateTest)
    FloatingActionButton fabCreateTest;
    @BindView(R.id.viewPager)
    ViewPager pager;
    Unbinder unbinder;
    RootFragmentPagerAdapter<CreateTestFragment> pagerAdapter;

    public CreateTestRootFragment() {
    }

    public static CreateTestRootFragment newInstance() {
        return new CreateTestRootFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_test_root, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pagerAdapter = new RootFragmentPagerAdapter<>(getChildFragmentManager());
        fabCreateTest.setOnClickListener(v ->
                getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.createRootHost, TabCreateTestFragment.newInstance("","", pagerAdapter))
                .commit());
        pager.setOffscreenPageLimit(100);
        pagerAdapter.addFragment(CreateTestFragment.newInstance("1"), 0);
        pagerAdapter.addFragment(CreateTestFragment.newInstance("2"), 1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == pagerAdapter.getCount() - 1) {
                    Observable timer = Observable.just(new Object())
                            .delay(350, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(o -> {
                                pagerAdapter.addFragment(CreateTestFragment.newInstance(String.valueOf(i + 2)), i + 1);
                                pagerAdapter.notifyDataSetChanged();
                            });
                    timer.subscribe();
                }
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}