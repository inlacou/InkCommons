package com.inlacou.inkbetterandroidviews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import timber.log.Timber

class GenericListAdapter<CustomView: View, CustomModel>(
	context: Context,
	private val itemList: List<CustomModel>,
	val layoutResourceId: Int,
	val onViewPopulate: (CustomView, CustomModel) -> Unit,
): ArrayAdapter<CustomModel>(context, layoutResourceId, itemList) {
	override fun getCount(): Int = itemList.size
	
	var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
	
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		Timber.d("getView $position | $itemList")
		val view = convertView ?: inflater.inflate(layoutResourceId, parent, false)
		onViewPopulate.invoke(view as CustomView, itemList[position])
		return view
	}
}