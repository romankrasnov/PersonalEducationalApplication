package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;

import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.Abstract.IAbstractModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.Abstract.IAbstractPresenter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.Abstract.IAbstractView;


public interface IMainActivityMVPprovider
{

    interface IPresenter extends IAbstractPresenter
    {
        void setView(IView view);
        void navigationItemSelected(int id);
    }

     interface IView extends NavigationView.OnNavigationItemSelectedListener, IAbstractView
     {
         void setMainFragment(Fragment fragment);
     }

    interface IModel extends IAbstractModel
    {

    }
}
