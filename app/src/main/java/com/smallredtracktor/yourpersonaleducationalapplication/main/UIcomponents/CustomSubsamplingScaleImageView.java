package com.smallredtracktor.yourpersonaleducationalapplication.main.UIcomponents;

import android.content.Context;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class CustomSubsamplingScaleImageView extends SubsamplingScaleImageView {
    public CustomSubsamplingScaleImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public CustomSubsamplingScaleImageView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetScaleAndCenter();
    }
}
