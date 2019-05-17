package com.smallredtracktor.yourpersonaleducationalapplication.main.Modules;


import android.content.Context;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Components.CreatingRootScope;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.SubjectTextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.TableDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.CreateTestRootDbImpl;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.ICreateTestRootDbApi;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IRootCreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Models.CreateTestRootFragmentModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters.CreateTestRootFragmentPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class CreateTestRootModule {

    private final Context context;

    public CreateTestRootModule(Context context) {
        this.context = context;
    }

    @Provides
    @CreatingRootScope
    IRootCreateTestFragmentMVPprovider.IPresenter provideCreateTestRootFragmentPresenter(IRootCreateTestFragmentMVPprovider.IModel model)
    {
        return new CreateTestRootFragmentPresenter(model);
    }

    @Provides
    @CreatingRootScope
    IRootCreateTestFragmentMVPprovider.IModel provideCreateTestRootFragmentModel(ICreateTestRootDbApi createTestRootDbApi)
    {
        return new CreateTestRootFragmentModel(createTestRootDbApi);
    }

    @Provides
    @CreatingRootScope
    SubjectTextDialog provideTextDialog(IRootCreateTestFragmentMVPprovider.IPresenter presenter)
    {
        return new SubjectTextDialog(presenter);
    }

    @Provides
    @CreatingRootScope
    ICreateTestRootDbApi provideCreateTestRootDbImpl()
    {
        return new CreateTestRootDbImpl(context);
    }

    @Provides
    @CreatingRootScope
    TableDialog provideTableDialog(IRootCreateTestFragmentMVPprovider.IPresenter presenter)
    {
        return new TableDialog(context, presenter);
    }

}
