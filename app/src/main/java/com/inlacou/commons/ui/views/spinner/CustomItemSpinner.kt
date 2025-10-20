package com.inlacou.commons.ui.views.spinner

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.inlacou.commons.R
import com.inlacou.commons.business.CustomItem
import com.inlacou.exinput.utils.extensions.setDrawableLeft
import com.inlacou.inkandroidextensions.dpToPx
import com.inlacou.inkandroidextensions.view.setDrawableRes
import com.inlacou.inkandroidextensions.view.tint
import com.inlacou.inkbetterandroidviews.adapters.GenericListAdapter
import com.inlacou.inkbetterandroidviews.spinners.BetterSpinner
import com.inlacou.pripple.RippleLinearLayout

/**
 * Created by inlacou on 26/08/21.
 */
class CustomItemSpinner @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BetterSpinner(context, attrs, defStyleAttr) {

	fun setItem(item: CustomItem?) {
		if(item==null) return
		setText(item.name)
		setDrawableLeft(ResourcesCompat.getDrawable(context.resources, resources.getIdentifier(item.iconResourceName, "drawable", context?.packageName), null)?.tint(item.colorHexadecimal), 16.dpToPx())
		dismissDropDown()
	}

	override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect)
	}

	fun setTagAdapter(items: List<CustomItem>) {
		setComplexAdapter(items) {
			GenericListAdapter(
				context, itemList = it.filterIsInstance<CustomItem>(),
				layoutResourceId = R.layout.view_tag_list,
				onViewPopulate = { base: RippleLinearLayout, item: CustomItem, pos: Int -> },
			).apply {
				this.onViewPopulate = { base: RippleLinearLayout, item: CustomItem, pos: Int ->
					base.findViewById<TextView>(R.id.tv_title).text = item.name
					resources.getIdentifier(item.iconResourceName, "drawable", context.packageName)
						.let { base.findViewById<ImageView>(R.id.iv_icon)?.setDrawableRes(if (it != 0) it else com.inlacou.inkbetterandroidviews.R.color.transparent) }
					base.findViewById<ImageView>(R.id.iv_icon)?.tint(item.colorHexadecimal)
					base.setOnClickListener {
						onItemClickListener.onItemClick(base.parent as ListView, it, pos, this.getItemId(pos))
						setItem(item)
					}
				}
			}
		}
	}
	
}