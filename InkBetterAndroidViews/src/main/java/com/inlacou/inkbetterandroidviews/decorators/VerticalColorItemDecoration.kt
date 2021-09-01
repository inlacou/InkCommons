package com.inlacou.inkbetterandroidviews.decorators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.inkandroidextensions.getColorCompat
import timber.log.Timber

@Deprecated("each time it is updated, the size is bigger because it accumulates. So... this is not a good idea.")
class VerticalColorItemDecoration(
    private val context: Context,
    private val top: Int, private val left: Int, private val right: Int, private val bottom: Int, private val backgroundResId: Int): RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val parentLeft = parent.left
            val parentRight = parent.right
            val parentTop = parent.top
            val parentBottom = parent.bottom

            val childLeft = child.left
            val childRight = child.right
            val childTop = child.top
            val childBottom = child.bottom

            Timber.d("$this | decoration | height: ${parent.height}")
            Timber.d("$this | decoration | width: ${parent.width}")
            Timber.d("$this | decoration | top decoration | from ${childTop-top-parent.paddingTop} ($childTop-$top-${parent.paddingTop}-${parent.marginTop}) to ${childTop+params.topMargin-parent.paddingTop} ($childTop+${params.topMargin}-${parent.paddingTop})")
            Timber.d("$this | decoration | bottom decoration | from ${childBottom+params.bottomMargin} ($childBottom+${params.bottomMargin}) to ${childBottom+bottom} ($childBottom+$childBottom)")

            paint.color = context.getColorCompat(backgroundResId)
            //Top
            c.drawRect((parent.paddingLeft+left).toFloat(), (childTop-top-parent.paddingTop).toFloat(), (parent.width-parent.paddingRight-right).toFloat(), (childTop+params.topMargin-parent.paddingTop).toFloat(), paint)
            //Bottom
            c.drawRect((parent.paddingLeft+left).toFloat(), (childBottom+params.bottomMargin).toFloat(), (parent.width-parent.paddingRight-right).toFloat(), (childBottom+bottom).toFloat(), paint)
            //Left
            c.drawRect((parent.paddingLeft).toFloat(), (childTop-top).toFloat(), (parent.paddingLeft+left).toFloat(), (childBottom+bottom).toFloat(), paint)
            //Right
            c.drawRect((parent.width-parent.paddingRight-right).toFloat(), (childTop-top).toFloat(), (parent.width-parent.paddingRight).toFloat(), (childBottom+bottom).toFloat(), paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = top
        outRect.left = left
        outRect.right = right
        outRect.bottom = bottom
    }
}