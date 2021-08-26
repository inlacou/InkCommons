package com.inlacou.inkbetterandroidviews.scrollviews

import android.content.Context
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import android.util.AttributeSet
import android.widget.OverScroller
import kotlin.math.abs

class BetterNestedScrollView(context: Context, attrs: AttributeSet) : NestedScrollView(context, attrs) {

	private val mScroller: OverScroller? = overScroller
	private var isFling = false

	private val overScroller: OverScroller?
		get() {
			return try {
				val fs = this.javaClass.superclass?.getDeclaredField("mScroller")
				fs?.isAccessible = true
				fs?.get(this) as OverScroller
			} catch (t: Throwable) {
				null
			}
		}

	override fun fling(velocityY: Int) {
		super.fling(velocityY)

		// here we effectively extend the super class functionality for backwards compatibility and just call invalidateOnAnimation()
		if (childCount > 0) {
			ViewCompat.postInvalidateOnAnimation(this)

			// Initializing isFling to true to track fling action in onScrollChanged() method
			isFling = true
		}
	}

	override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
		super.onScrollChanged(l, t, oldl, oldt)

		if (isFling) {
			if (abs(t - oldt) <= 3 || t == 0 || t == getChildAt(0).measuredHeight - measuredHeight) {
				isFling = false

				// This forces the mFinish variable in scroller to true (as explained the
				//    mentioned link above) and does the trick
				mScroller?.abortAnimation()
			}
		}
	}
}