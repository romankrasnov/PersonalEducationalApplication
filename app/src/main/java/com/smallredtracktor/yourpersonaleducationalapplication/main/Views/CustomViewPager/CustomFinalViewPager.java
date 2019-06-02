package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class CustomFinalViewPager extends CustomViewPager {
    public CustomFinalViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomFinalViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        beginFakeDrag();
        fakeDragBy(0);
        endFakeDrag();
    }
}
