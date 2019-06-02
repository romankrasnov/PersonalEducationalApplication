package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.PageTransformers;

import android.support.annotation.NonNull;
import android.view.View;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.CustomViewPager;

public class CardStackPageTransformer implements CustomViewPager.PageTransformer {

    private boolean mode;
    private static final float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(@NonNull View view, float position) {
        if(mode)
        {
            view.setAlpha(1f);
            view.setScaleX(1f);
            view.setScaleY(1f);
            view.setTranslationX(0f);
            view.setTranslationY(0f);
            if(position>=0)
            {
                view.setTranslationX(-view.getWidth()*position);
                view.setTranslationY(15*position);
            }
        } else
            {
                view.setAlpha(1f);
                view.setScaleX(1f);
                view.setScaleY(1f);
                view.setTranslationX(0f);
                view.setTranslationY(0f);
                int pageWidth = view.getWidth();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0f);

                } else if (position <= 0) { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    view.setAlpha(1f);
                    view.setTranslationX(0f);
                    view.setScaleX(1f);
                    view.setScaleY(1f);

                } else if (position <= 1) { // (0,1]
                    view.setAlpha(1 - position);
                    view.setTranslationX(pageWidth * -position);
                    float scaleFactor = MIN_SCALE
                            + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0f);
                }
            }
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }
}
