package com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders;




public interface ICreateTestFragmentMVPprovider {

    interface IPresenter
    {
        void onCreateTestFragmentInteraction();
        void onSwipe();
        void onAddQuestion();
        void onAddAnswer();
    }

    interface IFragment
    {

    }

    interface IModel
    {

    }
}
