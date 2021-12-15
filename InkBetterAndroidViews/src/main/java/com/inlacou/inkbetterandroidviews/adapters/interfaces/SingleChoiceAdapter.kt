package com.inlacou.project.template.list.interfaces

interface SingleChoiceAdapter<T: Selectable>: BaseAdapter {
	
	var currentlySelected: T?
	
	fun onItemSelected(item: T) {
		currentlySelected?.selected = false
		updateItemNullable(currentlySelected)
		currentlySelected = item
		currentlySelected?.selected = true
		updateItemNullable(currentlySelected)
	}
	
	fun updateItemNullable(item: T?) {
		if(item!=null){
			updateItem(item)
		}
	}
	
	/**
	 * Update item as needed
	 */
	fun updateItem(item: T)
}
