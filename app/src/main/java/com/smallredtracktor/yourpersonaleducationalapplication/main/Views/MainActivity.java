package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IMainActivityMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.root.App;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
        implements
        IMainActivityMVPprovider.IView,
        CreateTestFragment.OnFragmentInteractionListener,
        TrainingFragment.OnFragmentInteractionListener

{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Inject
    IMainActivityMVPprovider.IPresenter presenter;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

               App.get(this)
                .getComponent()
                .inject(this);
               
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
    }


    @Override
    protected void onResume() {
        presenter.setView(this);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        presenter.optionsItemSelected(id);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        presenter.navigationItemSelected(id);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void setMainFragment(Fragment fragment) {
    fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    if(fragmentManager.getFragments().isEmpty()) {
        fragmentTransaction.add(R.id.host, fragment);
    }else
        {
            if (!fragmentManager.getFragments().contains(fragment))
            {
                fragmentTransaction.replace(R.id.host, fragment);
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onCreateTestFragmentInteraction(Uri uri)
    {
        presenter.onCreateTestFragmentInteraction();
    }

    @Override
    public void onTrainingFragmentInteraction(Uri uri) {
        presenter.onTrainingFragmentInteraction();
    }
}
