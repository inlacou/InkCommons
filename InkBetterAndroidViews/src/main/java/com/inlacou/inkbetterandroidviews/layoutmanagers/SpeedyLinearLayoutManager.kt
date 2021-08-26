package com.inlacou.inkbetterandroidviews.layoutmanagers

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * LinearLayoutManager class that allows you to set the scroll speed. Bigger is slower. Default 100f, pretty smooth.
 */
class SpeedyLinearLayoutManager : LinearLayoutManager {
	
	private var speed: Float = MILLISECONDS_PER_INCH

	constructor(context: Context) : super(context)
	constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
	constructor(context: Context, orientation: Int, reverseLayout: Boolean, speed: Float) : super(context, orientation, reverseLayout) { this.speed = speed }
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
	
	override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
		val linearSmoothScroller = object : LinearSmoothScroller(recyclerView.context) {
			override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
				return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
			}
		}
		linearSmoothScroller.targetPosition = position
		startSmoothScroll(linearSmoothScroller)
	}
	
	companion object {
		private val MILLISECONDS_PER_INCH = 100f //default is 100f (bigger = slower)
	}
}