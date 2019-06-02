package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.Adapters;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.AnswerContentFragment;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.CustomFragmentStatePagerAdapter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.CustomViewPager;

import java.util.ArrayList;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.STUB_PARAM_ANSWER;
import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.STUB_PARAM_ID;

public class CustomFinalPageAdapter extends CustomFragmentStatePagerAdapter {

    private static final String STUB_PARAM_ID_DECOR = "decor";

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> fragmentsId = new ArrayList<>();
    private CustomViewPager viewPager;
    private ICreateTestFragmentMVPprovider.IPresenter presenter;
    private boolean isFullScreenMode = false;

    public CustomFinalPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setViewPager(CustomViewPager viewPager) {
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
    protected void handleGetItemInvalidated(View container, Fragment oldFragment, Fragment newFragment) {

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
        if(fragmentsId.contains(id) && !id.equals(STUB_PARAM_ID)) {
            position = fragmentsId.indexOf(id);
            Fragment oldFragment = fragments.get(position);
            this.destroyItem(viewPager, position, fragments.get(position));
            fragments.remove(oldFragment);
            fragmentsId.remove(id);
            this.notifyDataSetChanged();
            this.viewPager.setAdapter(this);
            this.notifyDataSetChanged();
            viewPager.setCurrentItem(1);
        }
    }

    public void addItem(String id, String param) {
        if(!fragmentsId.contains(id))
        {
            fragmentsId.add(1,id);
            fragments.add(1,AnswerContentFragment.newInstance(presenter,id, param, -1, isFullScreenMode));
            viewPager.setAdapter(this);
            this.notifyDataSetChanged();
            viewPager.setCurrentItem(1);
        }
    }

    public void setItem(String id, int type, String param) {
        int position;
        if(fragmentsId.contains(id)){
            position = fragmentsId.indexOf(id);
        } else
        {
            position = fragmentsId.indexOf(STUB_PARAM_ID);
        }
        Fragment oldFragment = fragments.get(position);
        Fragment newFragment = prepareFragment(id, type, param);
        replaceFragmetns(viewPager, oldFragment, newFragment);
        fragments.set(position,newFragment);
        fragmentsId.set(position, id);
    }

    private Fragment prepareFragment(String id, int type, String param) {
        return AnswerContentFragment.newInstance(presenter,id, param, type, isFullScreenMode);
    }

    public void addFirstItem() {
        fragmentsId.add(STUB_PARAM_ID_DECOR);
        fragments.add(new Fragment());
        fragmentsId.add(STUB_PARAM_ID);
        fragments.add(AnswerContentFragment.newInstance(presenter,  STUB_PARAM_ID, STUB_PARAM_ANSWER, -1, isFullScreenMode));
        fragmentsId.add(STUB_PARAM_ID_DECOR);
        fragments.add(new Fragment());
        this.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setViewMode(boolean isFullScreenMode) {
        this.isFullScreenMode = isFullScreenMode;
        this.notifyDataSetChanged();
            for (Fragment f: fragments) {
                if(f instanceof AnswerContentFragment) {
                    ((AnswerContentFragment) f).onViewModeChanged(isFullScreenMode);
                }
            }
    }

    public Fragment getItemById(String id) {
        return fragments.get(fragmentsId.indexOf(id));
    }


    public interface ViewModeChangeListener
    {
        void onViewModeChanged(boolean isFullScreenMode);
    }


}
