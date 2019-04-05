package com.smallredtracktor.yourpersonaleducationalapplication.main.Modules;


import android.content.Context;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Components.CreatingScope;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.CreateTestDbImpl;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.ICreateTestDbApi;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Models.CreateTestFragmentModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters.CreateTestFragmentPresenter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Repositories.CreateTestMemoryRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.ParseTextUtil;


import dagger.Module;
import dagger.Provides;

@Module
public class CreateTestModule {


    private final Context context;

    public CreateTestModule(Context context) {
        this.context = context;
    }

    @Provides
    @CreatingScope
    ChooseSourceDialog provideChooseSourceDialog(ICreateTestFragmentMVPprovider.IPresenter presenter)
    {
        return new ChooseSourceDialog(presenter);
    }

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
    ICreateTestRepository provideMemoryRepository(ICreateTestDbApi dbApi, ParseTextUtil util)
    {
        return new CreateTestMemoryRepository(dbApi,util);
    }

    @Provides
    @CreatingScope
    ICreateTestDbApi provideCreateTestDbApi()
    {
        return new CreateTestDbImpl(context);
    }

    @Provides
    @CreatingScope
    ParseTextUtil provideParseTextUtil()
    {
        return new ParseTextUtil();
    }

}
