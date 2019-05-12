package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class RootFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private List<T> mFragmentList = new ArrayList<>();
    private List<String> mIdList = new ArrayList<>();

    public RootFragmentPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(T fragment, int position, String s) {
        mFragmentList.add(position, fragment);
        mIdList.add(s);
    }

    public List<T> getItemList()
    {
        return mFragmentList;
    }

    public List<String> getIdList() {
        return mIdList;
    }
}
