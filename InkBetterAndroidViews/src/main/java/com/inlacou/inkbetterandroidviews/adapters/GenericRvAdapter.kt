package com.inlacou.inkbetterandroidviews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.inkbetterandroidviews.R

class GenericRvAdapter<CustomView: View, CustomModel>(
	private val itemList: List<CustomModel>,
	val layoutResourceId: Int,
	val onViewInitialize: (CustomView, View) -> Unit,
	val onViewPopulate: (CustomView, CustomModel) -> Unit,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	
	override fun getItemCount(): Int = this.itemList.size
	override fun getItemViewType(position: Int): Int = 0
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MyViewHolder(LayoutInflater.from(parent.context).inflate(layoutResourceId, parent, false))
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		onViewPopulate.invoke((holder as GenericRvAdapter<*, *>.MyViewHolder).view as CustomView, itemList[position])
	}
	
	inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val view: CustomView = itemView.findViewById(R.id.view)
		init { onViewInitialize.invoke(view, itemView) }
	}
	
}
