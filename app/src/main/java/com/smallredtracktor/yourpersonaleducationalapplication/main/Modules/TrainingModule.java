package com.smallredtracktor.yourpersonaleducationalapplication.main.Modules;

import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ITrainingFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Models.TrainingFragmentModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters.TrainingFragmentPresenter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ITrainingRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Repositories.TrainingRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class TrainingModule {

    @Provides
    ITrainingFragmentMVPprovider.IPresenter provideCreateTestFragmentPresenter(ITrainingFragmentMVPprovider.IModel model)
    {
        return new TrainingFragmentPresenter(model);
    }

    @Provides
    ITrainingFragmentMVPprovider.IModel provideCreateTestFragmentModel(ITrainingRepository repository)
    {
        return new TrainingFragmentModel(repository);
    }

    @Provides
    ITrainingRepository provideMemoryRepository()
    {
        return new TrainingRepository();
    }
}
