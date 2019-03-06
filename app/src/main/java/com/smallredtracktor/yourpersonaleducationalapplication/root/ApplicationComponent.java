package com.smallredtracktor.yourpersonaleducationalapplication.root;


import com.smallredtracktor.yourpersonaleducationalapplication.main.MainModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, MainModule.class})
    public interface ApplicationComponent {

        void inject(MainActivity target);
    }


