package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.RecycleViewPager.core

sealed class RVPageScrollState {
    class Idle: RVPageScrollState()
    class Dragging: RVPageScrollState()
    class Settling: RVPageScrollState()
}