package com.smallredtracktor.yourpersonaleducationalapplication.root;

import android.app.Application;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Components.CreateTestComponent;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.MainActivityModule;



public class App extends Application {

private static ApplicationComponent appComponent;
private CreateTestComponent createTestComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .mainActivityModule(new MainActivityModule())
                .build();
        }

        public static ApplicationComponent getComponent() {
            return appComponent;
        }


        public CreateTestComponent plusCreateTestComponent() {
                // always get only one instance
                if (createTestComponent == null) {
                        // start lifecycle of chatComponent
                        createTestComponent = appComponent.plusCreateTestComponent(new CreateTestModule());
                }
                return createTestComponent;
        }

        public void clearCreateTestComponent() {
                // end lifecycle of chatComponent
                createTestComponent = null;
        }

}


