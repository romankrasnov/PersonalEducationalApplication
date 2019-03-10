package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;


public interface IMainActivityMVPprovider
{

    interface IPresenter
    {
        void setView(IView view);
        ///With view EVENTS
        void navigationItemSelected(int id);
        void optionsItemSelected(int id);

        void onCreateTestFragmentInteraction();
        void onTrainingFragmentInteraction();
    }

     interface IView extends NavigationView.OnNavigationItemSelectedListener
     {
         void setMainFragment(Fragment fragment);
     }

    interface IModel
    {

    }
}
