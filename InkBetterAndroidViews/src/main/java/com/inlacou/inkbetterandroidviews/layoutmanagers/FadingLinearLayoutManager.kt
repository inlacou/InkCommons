package com.inlacou.inkbetterandroidviews.layoutmanagers

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.inkanimationtypes.Interpolable
import com.inlacou.inkanimationtypes.easetypes.EaseInOutCubic

class FadingLinearLayoutManager(
    context: Context,
    orientation: Int,
    reverseLayout: Boolean,
    val interpolator: Interpolable = EaseInOutCubic()
) : LinearLayoutManager(context, orientation, reverseLayout) {
    
    private var enabled = true
    private var recyclerView: RecyclerView? = null
    
    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        view?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                updateAll()
            }
        })
    }
    
    private fun setFading(index: Int, alpha: Float) {
        getChildAt(index)?.let {
            setFading(it, alpha)
        }
    }
    
    private fun setFading(view: View, alpha: Float) {
        if(view is LinearLayout) {
            val fadingViews = (0..view.childCount).mapNotNull { view.getChildAt(it) }.filterIsInstance<Fadeable>()
            if(fadingViews.isEmpty()) view.alpha = alpha
            else fadingViews.forEach { it.fade(alpha) }
        } else if(view is RelativeLayout) {
            val fadingViews = (0..view.childCount).mapNotNull { view.getChildAt(it) }.filterIsInstance<Fadeable>()
            if(fadingViews.isEmpty()) view.alpha = alpha
            else fadingViews.forEach { it.fade(alpha) }
        } else {
            if (view is Fadeable) view.fade(alpha)
            else view.alpha = alpha
        }
    }
    
    /**
     * Method to calculate how much of the view is visible
     */
    private fun getVisibleHeightPercentage(view: View): Float {
        val itemRect = Rect()
        val isVisible = view.getLocalVisibleRect(itemRect)

        //Find the height of the item.
        val visibleHeight = itemRect.height().toFloat()
        val expectedHeight = view.measuredHeight
        
        return if(isVisible) (visibleHeight / expectedHeight) /*view Visible Height Percentage*/
        else 1f
    }
    
    //update() is public so we can force update. When used on a list inside a MotionLayout, we will have to update manually each time the transition is updated, as the list will not be scrolling
    fun updateAll() {
        (0..childCount).mapNotNull { getChildAt(it) }.forEach { setFading(it, if(enabled) interpolator.getOffset(getVisibleHeightPercentage(it)) else 1f ) }
    }
    
    /*fun update() {
        val firstVisible = findFirstVisibleItemPosition()
        val firstVisibleComplete = findFirstCompletelyVisibleItemPosition()
        if(firstVisible==firstVisibleComplete) setFading(0, 1f)
        else getChildAt(0)?.let { setFading(it, interpolator.getOffset(getVisibleHeightPercentage(it))) }

        (1..childCount - 2).mapNotNull { getChildAt(it) }.forEach { setFading(it, 1f) }

        val lastVisible = findLastVisibleItemPosition()
        val lastVisibleComplete = findLastCompletelyVisibleItemPosition()
        if(lastVisible==lastVisibleComplete) setFading(childCount-1, 1f)
        else getChildAt(childCount-1)?.let { setFading(it, interpolator.getOffset(getVisibleHeightPercentage(it))) }
    }*/

	fun disable() {
		enabled = true
        updateAll()
	}
    
    fun enable() {
        enabled = true
        updateAll()
    }
	
	interface Fadeable {
        fun fade(visibility: Float)
    }
    
}