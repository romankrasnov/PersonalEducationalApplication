package com.smallredtracktor.yourpersonaleducationalapplication.root;

import android.app.Application;
import android.content.Context;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Components.CreateTestComponent;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Components.CreateTestRootComponent;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestRootModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.MainActivityModule;



public class App extends Application {

private ApplicationComponent appComponent;
private static CreateTestComponent createTestComponent;
private static CreateTestRootComponent createTestRootComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .mainActivityModule(new MainActivityModule())
                .build();
        }

        public ApplicationComponent getComponent() {
            return appComponent;
        }

        public static App get(Context context) {
        return (App) context.getApplicationContext();
    }



        public CreateTestComponent plusCreateTestComponent() {
                if (createTestComponent == null) {
                        createTestComponent = appComponent.plusCreateTestComponent(new CreateTestModule(this));
                }
                return createTestComponent;
        }

        public void clearCreateTestComponent() {
                createTestComponent = null;
        }

    public CreateTestRootComponent plusCreateTestRootComponent() {
        if (createTestRootComponent == null) {
            createTestRootComponent = appComponent.plusCreateTestRootComponent(new CreateTestRootModule(this));
        }
        return createTestRootComponent;
    }

    public void clearCreateTestRootComponent() {
        createTestRootComponent = null;
    }

}


