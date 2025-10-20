package com.inlacou.inkbetterandroidviews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class GenericListAdapter<CustomView: View, CustomModel>(
	context: Context,
	private val itemList: List<CustomModel>,
	val layoutResourceId: Int,
	var onViewPopulate: (view: CustomView, item: CustomModel, position: Int) -> Unit,
): ArrayAdapter<CustomModel>(context, layoutResourceId, itemList) {
	override fun getCount(): Int = itemList.size
	
	var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
	
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
		= (convertView ?: inflater.inflate(layoutResourceId, parent, false)).also {
			onViewPopulate.invoke(it as CustomView, itemList[position], position)
	}
}