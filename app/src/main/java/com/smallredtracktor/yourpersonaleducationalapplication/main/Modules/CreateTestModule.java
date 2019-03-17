package com.smallredtracktor.yourpersonaleducationalapplication.main.Modules;


import com.smallredtracktor.yourpersonaleducationalapplication.main.Components.CreatingScope;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Models.CreateTestFragmentModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters.CreateTestFragmentPresenter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Repositories.CreateTestMemoryRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;


import dagger.Module;
import dagger.Provides;

@Module
public class CreateTestModule {
    @Provides
    @CreatingScope
    ICreateTestFragmentMVPprovider.IPresenter provideCreateTestFragmentPresenter(ICreateTestFragmentMVPprovider.IModel model)
    {
        return new CreateTestFragmentPresenter(model);
    }

    @Provides
    @CreatingScope
    ICreateTestFragmentMVPprovider.IModel provideCreateTestFragmentModel(ICreateTestRepository repository)
    {
        return new CreateTestFragmentModel(repository);
    }

    @Provides
    @CreatingScope
    ICreateTestRepository provideMemoryRepository()
    {
        return new CreateTestMemoryRepository();
    }
}
