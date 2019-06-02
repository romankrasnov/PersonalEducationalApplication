package com.smallredtracktor.yourpersonaleducationalapplication.main.Modules;


import android.content.Context;


import com.smallredtracktor.yourpersonaleducationalapplication.main.Components.CreatingScope;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ChooseSourceDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.OcrDrawingDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.PhotoDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.ItemTextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.CreateTestDbImpl;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources.ICreateTestDbApi;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataStorages.ILocalStorage;
import com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataStorages.LocalStorage;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Models.CreateTestFragmentModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters.CreateTestFragmentPresenter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Repositories.CreateTestMemoryRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Repos.Interfaces.ICreateTestRepository;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CompressUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.GalleryPathUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.ParseTextUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.PolygonCropUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.PhotoIntentUtil;


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
    ICreateTestFragmentMVPprovider.IPresenter provideCreateTestFragmentPresenter(ICreateTestFragmentMVPprovider.IModel model,
                                                                                 CompressUtil compressUtil,
                                                                                 GalleryPathUtil galleryPathUtil,
                                                                                 PhotoIntentUtil photoIntentUtil,
                                                                                 PolygonCropUtil polygonCropUtil)
    {
        return new CreateTestFragmentPresenter(model, compressUtil, galleryPathUtil, photoIntentUtil, polygonCropUtil);
    }

    @Provides
    @CreatingScope
    ICreateTestFragmentMVPprovider.IModel provideCreateTestFragmentModel(ICreateTestRepository repository)
    {
        return new CreateTestFragmentModel(repository);
    }

    @Provides
    @CreatingScope
    ICreateTestRepository provideMemoryRepository(ICreateTestDbApi dbApi, ParseTextUtil util, ILocalStorage localStorage)
    {
        return new CreateTestMemoryRepository(dbApi, util, localStorage);
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

    @Provides
    @CreatingScope
    CompressUtil provideCompressUtil()
    {
        return new CompressUtil(context);
    }

    @Provides
    @CreatingScope
    GalleryPathUtil provideGalleryPathUtil()
    {
        return new GalleryPathUtil(context);
    }

    @Provides
    @CreatingScope
    PhotoIntentUtil providePhotoIntentUtil()
    {
        return new PhotoIntentUtil(context);
    }

    @Provides
    @CreatingScope
    ChooseSourceDialog provideChooseSourceDialog(ICreateTestFragmentMVPprovider.IPresenter presenter)
    {
        return new ChooseSourceDialog(presenter);
    }

    @Provides
    @CreatingScope
    PhotoDialog providePhotoDialog()
    {
        return new PhotoDialog(context);
    }

    @Provides
    @CreatingScope
    OcrDrawingDialog provideOcrDrawingDialog(ICreateTestFragmentMVPprovider.IPresenter presenter)
    {
        return new OcrDrawingDialog(context, presenter);
    }

    @Provides
    @CreatingScope
    ItemTextDialog provideTextDialog(ICreateTestFragmentMVPprovider.IPresenter presenter)
    {
        return new ItemTextDialog(presenter);
    }

    @Provides
    @CreatingScope
    ILocalStorage provideLocalStorage()
    {
        return new LocalStorage(context);
    }

    @Provides
    @CreatingScope
    PolygonCropUtil providePhotoCutUtil()
    {
        return new PolygonCropUtil();
    }
}
