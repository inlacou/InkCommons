package com.inlacou.inkbetterandroidviews.viewpagers

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

	override fun canScrollHorizontally(direction: Int): Boolean {
		return false
	}

	override fun onTouchEvent(ev: MotionEvent?): Boolean {
		return false
	}

	override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
		return false
	}

}