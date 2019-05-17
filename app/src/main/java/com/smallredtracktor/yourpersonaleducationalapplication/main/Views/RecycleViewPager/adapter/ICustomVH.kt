package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.RecycleViewPager.adapter


interface ICustomVH {
    fun bind(model: PageModel)
    fun onSelected()
}