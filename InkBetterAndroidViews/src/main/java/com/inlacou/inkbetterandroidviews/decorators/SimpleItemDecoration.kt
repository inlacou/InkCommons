package com.inlacou.inkbetterandroidviews.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.inkandroidextensions.dpToPx

class SimpleItemDecoration(private val top: Int, private val left: Int, private val right: Int, private val bottom: Int, private val unit: Unit = Unit.PX): RecyclerView.ItemDecoration() {

    enum class Unit {
        DP, PX
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = when(unit){
            Unit.DP -> left.dpToPx()
            Unit.PX -> left
        }
        outRect.right = when(unit){
            Unit.DP -> right.dpToPx()
            Unit.PX -> right
        }
        outRect.bottom = when(unit){
            Unit.DP -> bottom.dpToPx()
            Unit.PX -> bottom
        }
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = when(unit){
                Unit.DP -> top.dpToPx()
                Unit.PX -> top
            }
        }
    }
}