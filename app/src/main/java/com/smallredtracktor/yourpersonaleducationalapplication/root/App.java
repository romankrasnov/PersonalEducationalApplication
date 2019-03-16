package com.smallredtracktor.yourpersonaleducationalapplication.root;

import android.app.Application;
import android.content.Context;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Components.CreateTestComponent;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.MainActivityModule;



public class App extends Application {

private ApplicationComponent appComponent;
private static CreateTestComponent createTestComponent;

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
                // always get only one instance
                if (createTestComponent == null) {
                        // start lifecycle
                        createTestComponent = appComponent.plusCreateTestComponent(new CreateTestModule());
                }
                return createTestComponent;
        }

        public void clearCreateTestComponent() {
                // end lifecycle of chatComponent
                createTestComponent = null;
        }

}


