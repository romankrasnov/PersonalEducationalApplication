package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IMainActivityMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestRootFragment;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.TrainingFragment;


import javax.annotation.Nullable;

public class MainActivityPresenter implements IMainActivityMVPprovider.IPresenter {

    @Nullable
    private IMainActivityMVPprovider.IView view;
    private IMainActivityMVPprovider.IModel model;


    public MainActivityPresenter (IMainActivityMVPprovider.IModel model) {
        this.model = model;
    }

    @Override
    public void setView(IMainActivityMVPprovider.IView view) {
        this.view = view;
    }


    @Override
    public void navigationItemSelected(int id) {

        if (view != null) {
            if (id == R.id.nav_training) { view.setMainFragment(TrainingFragment.newInstance("", "")); }
            else if (id == R.id.nav_create) { view.setMainFragment(CreateTestRootFragment.newInstance(UniqueDigit.getUnique())); }
            else if (id == R.id.nav_statistics) { }
            else if (id == R.id.nav_manage) {}
            else if (id == R.id.nav_share) {}
        }
    }

    @Override
    public void optionsItemSelected(int id) {
        if (id == R.id.action_settings) {

        }
    }

}
