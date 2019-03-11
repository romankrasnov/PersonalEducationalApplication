package com.smallredtracktor.yourpersonaleducationalapplication.root;




import com.smallredtracktor.yourpersonaleducationalapplication.main.Components.CreateTestComponent;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.MainActivityModule;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.MainActivity;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, MainActivityModule.class})
public interface ApplicationComponent {

    CreateTestComponent plusCreateTestComponent(CreateTestModule createTestModule);

    void inject(MainActivity target);

    }

