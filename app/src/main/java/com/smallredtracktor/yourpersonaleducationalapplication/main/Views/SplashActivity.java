package com.smallredtracktor.yourpersonaleducationalapplication.main.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.MainActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

