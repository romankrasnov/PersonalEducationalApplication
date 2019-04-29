package com.smallredtracktor.yourpersonaleducationalapplication.main.Components;

import android.content.Context;


import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment;



import dagger.Subcomponent;

@CreatingScope
@Subcomponent( modules = {CreateTestModule.class})

public interface CreateTestComponent {

    Context context();
    void inject(CreateTestFragment target);
}

                                                                                      

