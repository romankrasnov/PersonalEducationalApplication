package com.smallredtracktor.yourpersonaleducationalapplication.root;

import android.app.Application;


import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.MainActivityModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.TrainingModule;


public class App extends Application {

private ApplicationComponent component;

@Override
public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .mainActivityModule(new MainActivityModule())
        .createTestModule(new CreateTestModule())
        .trainingModule(new TrainingModule())
        .build();
        }

public ApplicationComponent getComponent() {
        return component;
        }

}


