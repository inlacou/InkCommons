package com.inlacou.project.template.list.interfaces

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface BaseAdapter {
	
	val recyclerView: RecyclerView?
	
	fun getViewAt(index: Int): View {
		return (0..(recyclerView?.layoutManager?.childCount ?: 0)).mapNotNull { recyclerView?.getChildAt(it) }[index]
	}
}