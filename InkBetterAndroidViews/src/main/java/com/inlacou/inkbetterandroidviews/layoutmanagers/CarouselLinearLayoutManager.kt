package com.inlacou.inkbetterandroidviews.layoutmanagers

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.min


class CarouselLinearLayoutManager: LinearLayoutManager {

    constructor(context: Context, orientation: Int, reverseLayout: Boolean): super(context, orientation, reverseLayout)
    constructor(context: Context, orientation: Int, reverseLayout: Boolean, scaleOnScroll: Boolean): super(context, orientation, reverseLayout) {
        this.scaleOnScroll = scaleOnScroll
    }
    constructor(context: Context, orientation: Int, reverseLayout: Boolean, scalingFactor: Float): super(context, orientation, reverseLayout) {
        this.scalingFactor = scalingFactor
    }
    constructor(context: Context, orientation: Int, reverseLayout: Boolean, scaleOnScroll: Boolean, scalingFactor: Float): super(context, orientation, reverseLayout) {
        this.scaleOnScroll = scaleOnScroll
        this.scalingFactor = scalingFactor
    }
    
    var scaleOnScroll = false
    
    /**
     * 0f is no scale, bigger the number, scale more.
     */
    var scalingFactor = 0f

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        scrollHorizontallyBy(0, recycler, state)
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
        
        if (scaleOnScroll) {
            try {
                for (i in 0 until childCount) {
                    getChildAt(i)?.let { child ->
                        val childWidth = child.right - child.left.toFloat()
                        val childWidthHalf = childWidth / 2f
                        val childCenter = child.left + childWidthHalf
                
                        val parentWidth = width.toFloat()
                        val parentWidthHalf = parentWidth / 2f
                
                        val d0 = 0f
                        val mShrinkDistance = .75f
                        val d1 = mShrinkDistance * parentWidthHalf
                
                        val s0 = 1f
                        val mShrinkAmount = scalingFactor
                        val s1 = 1f - mShrinkAmount
                        
                        val d = min(d1, abs(parentWidthHalf - childCenter))
                        
                        //wtf
                        val scalingFactor = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                        
                        child.resizeView(scalingFactor, true)
                    }
                }
            } catch (e: Exception) {
                //Timber.e(e)
                e.printStackTrace()
            }
        }
        return scrolled
    }
    
    private var maxWidth: Int? = null
    private var maxHeight: Int? = null
    
    private fun View.resizeView(scaleFactor: Float, workAroundCenterVertical: Boolean = false) {
        val lp = layoutParams ?: return
        if(maxWidth==null) {
            maxWidth = this.width
            maxHeight = this.height
        }
        val targetWidth = (maxWidth!! * scaleFactor).toInt()
        val targetHeight = (maxHeight!! * scaleFactor).toInt()
        if (lp.width.compareWithThreshold(targetWidth, 0)) return
        if (scaleFactor >= 0) {
            lp.height = targetHeight
            lp.width = targetWidth
        }
        if (workAroundCenterVertical && lp is ViewGroup.MarginLayoutParams) {
            val verticalMargin = (maxHeight!! - targetHeight) / 2
            lp.setMargins(10, verticalMargin, 10, verticalMargin)
        }
        layoutParams = lp
    }
    
    fun Int.compareWithThreshold(other: Int, threshold: Int): Boolean {
        return this>=other-threshold && this<other+threshold
    }
}