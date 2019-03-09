package com.smallredtracktor.yourpersonaleducationalapplication.root;



import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.MainActivityModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.TrainingModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, MainActivityModule.class, CreateTestModule.class, TrainingModule.class})

public interface ApplicationComponent {

        void inject(MainActivity target);

    }


