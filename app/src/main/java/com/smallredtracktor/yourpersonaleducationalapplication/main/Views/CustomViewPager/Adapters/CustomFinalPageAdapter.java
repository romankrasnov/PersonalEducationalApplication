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

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.STUB_PARAM;
import static com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment.STUB_PARAM_ID;

public class CustomFinalPageAdapter extends CustomFragmentStatePagerAdapter {

    private static final String STUB_PARAM_ID_DECOR = "decor";
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> fragmentsId = new ArrayList<>();
    private CustomViewPager viewPager;
    private ICreateTestFragmentMVPprovider.IPresenter presenter;
    private boolean isFullScreenMode;


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
            setViewMode(isFullScreenMode);
        }
    }

    public void addItem(String id, String param) {
        if(!fragmentsId.contains(id))
        {
            //remove old decor
            int lastDecorPagePos = fragments.size() - 1;
            this.destroyItem(viewPager, lastDecorPagePos, fragments.get(lastDecorPagePos));
            fragments.remove(lastDecorPagePos);
            fragmentsId.remove(lastDecorPagePos);
            this.notifyDataSetChanged();
            this.viewPager.setAdapter(this);
            this.notifyDataSetChanged();

            fragmentsId.add(id);
            fragments.add(AnswerContentFragment.newInstance(presenter,id, param, -1));
            this.notifyDataSetChanged();

            //decor
            fragmentsId.add(STUB_PARAM_ID_DECOR);
            fragments.add(new Fragment());
            this.notifyDataSetChanged();
            viewPager.setCurrentItem(lastDecorPagePos-1);
            setViewMode(isFullScreenMode);
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
        fragmentsId.set(position,id);
        setViewMode(isFullScreenMode);
    }

    private Fragment prepareFragment(String id, int type, String param) {
        return AnswerContentFragment.newInstance(presenter,id, param, type);
    }

    public void addFirstItem() {
        fragmentsId.add(STUB_PARAM_ID_DECOR);
        fragments.add(new Fragment());
        fragmentsId.add(STUB_PARAM_ID);
        fragments.add(AnswerContentFragment.newInstance(presenter,  STUB_PARAM_ID, STUB_PARAM, -1));
        fragmentsId.add(STUB_PARAM_ID_DECOR);
        fragments.add(new Fragment());
        this.notifyDataSetChanged();
    }


    public void setViewMode(boolean isFullScreenMode) {
        this.isFullScreenMode = isFullScreenMode;
            for (Fragment f: fragments) {
                if(f instanceof AnswerContentFragment) {
                    ((AnswerContentFragment) f).onViewModeChanged(isFullScreenMode);
                    this.notifyDataSetChanged();
                }
            }
    }


    public interface ViewModeChangeListener
    {
        void onViewModeChanged(boolean isFullScreenMode);
    }


}
