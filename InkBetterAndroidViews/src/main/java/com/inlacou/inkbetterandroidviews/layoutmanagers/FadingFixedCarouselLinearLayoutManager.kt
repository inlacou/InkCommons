package com.inlacou.inkbetterandroidviews.layoutmanagers

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.inkanimationtypes.Interpolable
import com.inlacou.inkanimationtypes.easetypes.EaseOutQuart
import com.inlacou.inkanimationtypes.easetypes.Linear
import kotlin.math.abs
import kotlin.math.min

class FadingFixedCarouselLinearLayoutManager(
    context: Context,
    reverseLayout: Boolean,
    val shrinkInterpolator: Interpolable = Linear(),
    val fadingInterpolator: Interpolable = EaseOutQuart(),
    val maxShrink: Float = 0.4f,
    val minAlpha: Float = -0.0f,
): LinearLayoutManager(context, HORIZONTAL, reverseLayout) {
    
    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        view?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                update()
            }
        })
    }
    
    private fun getFadingFactor(child: View): Float {
        val childWidth = child.right - child.left.toFloat()
        val childCenter = child.left + (childWidth / 2f)
    
        val parentWidth = width.toFloat()
        val parentWidthHalf = parentWidth / 2f
    
        val d0 = 0f
        val maxShrinkDistance = .7f /*Distance until max shrink*/
        val d1 = maxShrinkDistance * parentWidthHalf
        
        val s0 = 3f
        val s1 = minAlpha
    
        val d = min(d1, abs(parentWidthHalf - childCenter))
    
        //wtf
        return s0 + (s1 - s0) * (d - d0) / (d1 - d0)
    }
    
    private fun getScalingFactor(child: View): Float {
        val childWidth = child.right - child.left.toFloat()
        val childCenter = child.left + (childWidth / 2f)
    
        val parentWidth = width.toFloat()
        val parentWidthHalf = parentWidth / 2f
    
        val d0 = 0f
        val mShrinkDistance = .75f /*Distance until max shrink*/
        val d1 = mShrinkDistance * parentWidthHalf
    
        val s0 = 1f
        val s1 = maxShrink
    
        val d = min(d1, abs(parentWidthHalf - childCenter))
    
        //wtf
        return s0 + (s1 - s0) * (d - d0) / (d1 - d0)
    }
    
    fun update() {
        for (i in 0 until childCount) {
            getChildAt(i)?.let {
                setShrink(it, shrinkInterpolator.getOffset(getScalingFactor(it)))
                setFading(it, fadingInterpolator.getOffset(getFadingFactor(it)))
            }
        }
    }
    
    private fun setShrink(index: Int, alpha: Float) {
        getChildAt(index)?.let { if(it is Shrinkable) it.shrink(alpha) }
    }
    
    private fun setShrink(view: View, alpha: Float) {
        if(view is Shrinkable) view.shrink(alpha)
    }
    
    private fun setFading(index: Int, alpha: Float) {
        getChildAt(index)?.let {
            if(it is Fadeable) it.fade(alpha)
            else it.alpha = alpha
        }
    }
    
    private fun setFading(view: View, alpha: Float) {
        view.let {
            if(it is Fadeable) it.fade(alpha)
            else it.alpha = alpha
        }
    }
    
    interface Shrinkable {
        fun shrink(percentage: Float)
    }
    
    interface Fadeable {
        fun fade(visibility: Float)
    }
    
}