package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;


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
import android.view.MenuItem;

import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IMainActivityMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.root.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.jonasrottmann.realmbrowser.RealmBrowser;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity
        implements
        IMainActivityMVPprovider.IView
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

    private List<BackPressedListener> backPressedListeners = new ArrayList<>();
    public void addBackPressedListener(BackPressedListener listener)
    {
        backPressedListeners.add(listener);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            for (BackPressedListener listener: backPressedListeners) {
                listener.onBackPressed();
            }
            //super.onBackPressed();
        }
    }



    public interface BackPressedListener
    {
        void onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_statistics)
        {
            //for db test
            Realm.init(this);
            Realm realm = Realm.getDefaultInstance();
            RealmConfiguration configuration = realm.getConfiguration();
            realm.close();
            RealmBrowser.startRealmModelsActivity(this, configuration);
        } else
            {
                presenter.navigationItemSelected(id);
            }
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
        fragmentTransaction.addToBackStack("first");
        fragmentTransaction.commit();
    }
}
