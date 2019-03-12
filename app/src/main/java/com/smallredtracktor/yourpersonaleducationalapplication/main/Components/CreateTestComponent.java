package com.smallredtracktor.yourpersonaleducationalapplication.main.Components;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Modules.CreateTestModule;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CreateTestFragment;



import dagger.Subcomponent;

@TrainingScope
@Subcomponent(modules = {CreateTestModule.class})

public interface CreateTestComponent {

    void inject(CreateTestFragment target);
}



