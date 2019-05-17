package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;

import android.support.annotation.Nullable;

import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.Abstract.IAbstractModel;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.Abstract.IAbstractPresenter;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.Abstract.IAbstractView;

import java.util.List;


public interface IRootCreateTestFragmentMVPprovider {

    interface IPresenter extends IAbstractPresenter
    {
        void setView(@Nullable IFragment view);
        void onTableFabClick();
        void onPageSelected(int i, int count);
        void onViewCreated();
        void onSaveFabClick(List<String> strings, String id);
    }

    interface IFragment extends IAbstractView
    {
        void showTable();
        void addFragmentToPageAdapter(int i, String id);
        void setCurrentPage(int position);
        void showTextDialog(String text, String id, List<String> strings);
        void close();
        void hideTable();
    }

    interface IModel extends IAbstractModel
    {
        void writeTickets(List<String> strings, String id);
        void writeSubject(String id, String text);
    }
}
