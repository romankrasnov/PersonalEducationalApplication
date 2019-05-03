package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager;

import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.WindowInsetsCompat;
import android.view.View;

interface OnApplyWindowInsetsCompatListener extends OnApplyWindowInsetsListener {
    WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat originalInsets);
}

