package com.smallredtracktor.yourpersonaleducationalapplication.main.Presenters;

import android.support.annotation.Nullable;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs.SubjectTextDialog;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.IRootCreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.TabCreateTestFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateTestRootFragmentPresenter implements
        IRootCreateTestFragmentMVPprovider.IPresenter,
        TabCreateTestFragment.TabCreateTestFragmentListener,
        SubjectTextDialog.TextDialogListener {


    private final IRootCreateTestFragmentMVPprovider.IModel model;
    private IRootCreateTestFragmentMVPprovider.IFragment view;


    public CreateTestRootFragmentPresenter(IRootCreateTestFragmentMVPprovider.IModel model) {
        this.model = model;
    }

    @Override
    public void setView(@Nullable IRootCreateTestFragmentMVPprovider.IFragment view) {
        this.view = view;
    }

    @Override
    public void onTableFabClick() {
        view.showTable();
    }

    @Override
    public void onPageSelected(int i, int count) {
        if (i == count - 1) {
            Observable timer = Observable.just(new Object())
                    .delay(300, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(o -> view.addFragmentToPageAdapter(i + 1));
            timer.subscribe();
        }
    }

    @Override
    public void onViewCreated() {
        Observable.just(0, 1)
                .delay(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> view.addFragmentToPageAdapter(i))
                .subscribe();
    }

    @Override
    public void onSaveFabClick(List<String> strings, String id) {
        view.showTextDialog("", id, strings);

    }


    @Override
    public void onTableItemInteraction(int position) {
        view.hideTable();
        view.setCurrentPage(position);
    }

    @Override
    public void onTextDialogCreate(String id) {

    }

    @Override
    public void onTextDialogInteraction(String id, String text, List<String> strings) {
        if (text != null)
        {
            model.writeSubject(id, text);
            model.writeTickets(strings, id);
            view.close();
        }
    }
}
