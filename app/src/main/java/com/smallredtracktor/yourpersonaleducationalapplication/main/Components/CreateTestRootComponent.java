package com.smallredtracktor.yourpersonaleducationalapplication.main.Components;

import android.content.Context;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestRootModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestRootFragment;

import dagger.Subcomponent;

@CreatingRootScope
@Subcomponent(modules = {CreateTestRootModule.class})
public interface CreateTestRootComponent {
    Context context();
    void inject(CreateTestRootFragment target);
}
