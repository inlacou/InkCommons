package com.inlacou.inkbetterandroidviews.decorators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.inkandroidextensions.getColorCompat

@Deprecated("each time it is updated, the size is bigger because it accumulates. So... this is not a good idea.")
class HorizontalColorItemDecoration(
    private val context: Context,
    private val top: Int = 0, private val left: Int = 0, private val right: Int = 0, private val bottom: Int = 0, private val backgroundResId: Int): RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            paint.color = context.getColorCompat(backgroundResId)
            //Top
            c.drawRect((child.paddingLeft+left).toFloat(), (parent.top-top).toFloat(), (child.width-child.paddingRight-right).toFloat(), (parent.top+params.topMargin).toFloat(), paint)
            //Bottom
            c.drawRect((child.paddingLeft+left).toFloat(), (parent.bottom+params.bottomMargin).toFloat(), (child.width-child.paddingRight-right).toFloat(), (parent.bottom+bottom).toFloat(), paint)
            //Left
            c.drawRect((child.paddingLeft).toFloat(), (parent.top-top).toFloat(), (child.paddingLeft+left).toFloat(), (parent.bottom+bottom).toFloat(), paint)
            //Right
            c.drawRect((child.width-parent.paddingRight-right).toFloat(), (parent.top-top).toFloat(), (child.width-child.paddingRight).toFloat(), (parent.bottom+bottom).toFloat(), paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = top
        outRect.left = left
        outRect.right = right
        outRect.bottom = bottom
    }
}