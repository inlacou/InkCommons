package com.inlacou.inkbetterandroidviews.scrollviews

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * Triggers a event when scrolling reaches bottom.
 *
 * Created by martinsandstrom on 2010-05-12.
 * Updated by martinsandstrom on 2014-07-22.
 *
 * Usage:
 *
 * scrollView.setOnBottomReachedListener(
 *      new InteractiveScrollView.OnBottomReachedListener() {
 *          @Override
 *          public void onBottomReached() {
 *              // do something
 *          }
 *      }
 * );
 *
 */
class InteractiveScrollView : ScrollView {
	// Getters & Setters

	var onBottomReachedListener: OnBottomReachedListener? = null

	constructor(context: Context, attrs: AttributeSet,
	            defStyle: Int) : super(context, attrs, defStyle)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context) : super(context)

	override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
		val view = getChildAt(childCount - 1)
		val diff = view.bottom - (height + scrollY)

		if (diff==0 && onBottomReachedListener != null) {
			onBottomReachedListener?.onBottomReached()
		}

		super.onScrollChanged(l, t, oldl, oldt)
	}

	/**
	 * Event listener.
	 */
	interface OnBottomReachedListener {
		fun onBottomReached()
	}

}