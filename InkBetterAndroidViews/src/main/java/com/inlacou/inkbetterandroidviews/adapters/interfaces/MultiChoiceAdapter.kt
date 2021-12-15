package com.inlacou.project.template.list.interfaces

interface MultiChoiceAdapter<T: Selectable>: BaseAdapter {
	
	val currentlySelected: MutableList<T>
	
	fun onItemSelected(item: T, allowDeselect: Boolean) {
		if(currentlySelected.contains(item)) {
			if(allowDeselect) {
				item.selected = false
				currentlySelected.remove(item)
			}
		}else{
			item.selected = true
			currentlySelected.add(item)
		}
		updateItem(item)
	}
	
	/**
	 * Update item as needed
	 */
	fun updateItem(item: T)
	
}
