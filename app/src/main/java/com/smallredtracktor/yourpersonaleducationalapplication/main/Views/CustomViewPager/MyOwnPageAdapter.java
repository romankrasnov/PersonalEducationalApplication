package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;

import java.util.ArrayList;
import java.util.List;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.STUB_PARAM;
import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.STUB_PARAM_ID;

public class MyOwnPageAdapter extends MyFragmentStatePagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> answersId = new ArrayList<>();
    private MyViewPager viewPager;
    private ICreateTestFragmentMVPprovider.IPresenter presenter;

    public MyOwnPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setViewPager(MyViewPager viewPager) {
        this.viewPager = viewPager;
    }


    public void setPresenter(ICreateTestFragmentMVPprovider.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Fragment getItem(int var1) {
        return fragments.get(var1);
    }

    @Override
    protected void handleGetItemInbalidated(View container, Fragment oldFragment, Fragment newFragment) {

    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        return fragments.indexOf(fragment);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull View container, int position) {
        if (position == getCount() - 1) {
            if (fragments.get(position) == null) {
                return super.instantiateItem(container, position);
            } else {
                return fragments.get(position);
            }
        } else {
            return super.instantiateItem(container, position);
        }
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        if (position != getCount() - 1) {
            super.destroyItem(container, position, object);
        }
    }

    public void removeItem(String id) {
        int position;
        if(answersId.contains(id) && !id.equals(STUB_PARAM_ID)) {
            position = answersId.indexOf(id);
            Fragment oldFragment = fragments.get(position);
            this.destroyItem(viewPager, position, fragments.get(position));
            fragments.remove(oldFragment);
            answersId.remove(id);
            this.notifyDataSetChanged();
            this.viewPager.setAdapter(this);
            this.notifyDataSetChanged();
        }
    }

    public void addItem(String id, String param) {
        if(!answersId.contains(id))
        {
            answersId.add(id);
            fragments.add(AnswerContentFragment.newInstance(presenter,id, param, -1));
            this.notifyDataSetChanged();
        }
    }

    public void setItem(String id, int type, String param) {
        int position;
        if(answersId.contains(id)){
            position = answersId.indexOf(id);
        } else
        {
            position = answersId.indexOf(STUB_PARAM_ID);
        }
        Fragment oldFragment = fragments.get(position);
        Fragment newFragment = prepareFragment(id, type, param);
        replaceFragmetns(viewPager, oldFragment, newFragment);
        fragments.set(position,newFragment);
        answersId.set(position,id);
    }

    private Fragment prepareFragment(String id, int type, String param) {
        return AnswerContentFragment.newInstance(presenter,id, param, type);
    }

    public void addFirstItem() {
        answersId.add(STUB_PARAM_ID);
        fragments.add(AnswerContentFragment.newInstance(presenter,  STUB_PARAM_ID, STUB_PARAM, -1));
        this.notifyDataSetChanged();
    }
}
