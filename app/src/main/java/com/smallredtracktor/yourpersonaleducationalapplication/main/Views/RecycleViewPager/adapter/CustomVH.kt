package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.RecycleViewPager.adapter


import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smallredtracktor.yourpersonaleducationalapplication.R
import com.smallredtracktor.yourpersonaleducationalapplication.main.Views.CustomViewPager.CustomViewPager


class CustomVH : RecyclerView.ViewHolder, ICustomVH {
    private val ticketCounterTextView: TextView
    private val questionTextView: TextView
    private val questionImageView: SubsamplingScaleImageView
    private val viewPagerLayout: FrameLayout
    private val smallViewPager: CustomViewPager
    private val createTestRootConstraintLayout: ConstraintLayout

    constructor(inflater: LayoutInflater, parent: ViewGroup)
            : this(inflater.inflate(R.layout.fragment_create_test, parent, false) as ViewGroup)

    constructor(listPageView: ViewGroup)
            : super(listPageView) {
        ticketCounterTextView = listPageView.findViewById(R.id.ticketCounterTextView)
        questionTextView = listPageView.findViewById(R.id.questionTextView)
        questionImageView = listPageView.findViewById(R.id.answerImageView)
        viewPagerLayout = listPageView.findViewById(R.id.viewPagerLayout)
        smallViewPager = listPageView.findViewById(R.id.smallViewPager)
        createTestRootConstraintLayout = listPageView.findViewById(R.id.createTestRootConstraintLayout)
    }

    override fun bind(model: PageModel) {
        mPageIndexText.text = model.name
    }


    override fun onSelected() {
        /*
        val scaleXAnim = ObjectAnimator.ofFloat(mPageIndexText, View.SCALE_X, 0.75f, 1.8f, 1f, 0.8f, 1f)
        val scaleYAnim = ObjectAnimator.ofFloat(mPageIndexText, View.SCALE_Y, 0.75f, 1.8f, 1f, 0.8f, 1f)
        val animation = AnimatorSet()
        animation.playTogether(scaleXAnim, scaleYAnim)
        animation.duration = 600
        animation.start()
        */
    }
}