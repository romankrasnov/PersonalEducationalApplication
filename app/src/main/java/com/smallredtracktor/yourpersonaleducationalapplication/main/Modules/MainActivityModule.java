package com.smallredtracktor.yourpersonaleducationalapplication.main.Modules;



import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IMainActivityMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Models.MainActivityModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters.MainActivityPresenter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.IMainActivityRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Repositories.MemoryRepository;


import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

        @Provides
        IMainActivityMVPprovider.IPresenter provideMainActivityPresenter(IMainActivityMVPprovider.IModel model)
        {
            return new MainActivityPresenter(model);
        }

        @Provides
        IMainActivityMVPprovider.IModel provideMainActivityModel(IMainActivityRepository repository)
        {
            return new MainActivityModel(repository);
        }

        @Provides
        IMainActivityRepository provideMemoryRepository()
        {
            return new MemoryRepository();
        }

    }

