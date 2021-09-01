package com.inlacou.inkbetterandroidviews.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.inkandroidextensions.dpToPx

class BottomOffsetItemDecoration(private val offset: Int, private val unit: Unit = Unit.PX): RecyclerView.ItemDecoration() {

    enum class Unit {
        DP, PX
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // Add top margin only for the last item
        if (parent.getChildAdapterPosition(view)==state.itemCount-1) {
            outRect.bottom = when (unit) {
                Unit.DP -> offset.dpToPx()
                Unit.PX -> offset
            }
        }
    }
}