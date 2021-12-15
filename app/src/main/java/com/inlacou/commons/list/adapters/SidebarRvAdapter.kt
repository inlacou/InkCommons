package com.inlacou.commons.list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.commons.R
import com.inlacou.commons.ui.views.sidebar.row.SidebarRowView
import com.inlacou.project.template.list.interfaces.SingleChoiceAdapter
import com.inlacou.commons.ui.views.sidebar.SidebarViewMdl
import com.inlacou.commons.ui.views.sidebar.title.SidebarTitleView

class SidebarRvAdapter(override val recyclerView: RecyclerView?, private val itemList: MutableList<SidebarViewMdl>)
	: RecyclerView.Adapter<RecyclerView.ViewHolder>(), SingleChoiceAdapter<SidebarViewMdl> {
	override var currentlySelected: SidebarViewMdl? = null
	
	override fun updateItem(item: SidebarViewMdl) {
		getViewAt(itemList.indexOf(item)).apply {
			if(this is SidebarTitleView) populate()
			if(this is SidebarRowView) populate()
		}
	}
	private var lastAdded: Int = 0

	override fun getItemViewType(position: Int): Int {
		return if(itemList[position].item.mdl.isTitle) 0
		else 1
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return when (viewType) {
			0 -> MyViewHolder1(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_sidebar_title, parent, false))
			else -> MyViewHolder2(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_sidebar_row, parent, false))
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when(getItemViewType(position)) {
			0 -> {
				val viewHolder = holder as MyViewHolder1
				viewHolder.view.model = itemList[position]
				if (position > lastAdded) {
					viewHolder.view.inAnimation()
					lastAdded = position
				}
			}
			else -> {
				val viewHolder = holder as MyViewHolder2
				viewHolder.view.model = itemList[position]
				if (position > lastAdded) {
					viewHolder.view.inAnimation()
					lastAdded = position
				}
			}
		}
	}

	inner class MyViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val view: SidebarTitleView = itemView.findViewById(R.id.view)
	}

	inner class MyViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val view: SidebarRowView = itemView.findViewById(R.id.view)
	}

	override fun getItemCount(): Int {
		return this.itemList.size
	}
}
