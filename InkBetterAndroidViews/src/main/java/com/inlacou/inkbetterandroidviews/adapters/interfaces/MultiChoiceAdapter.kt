package com.inlacou.inkbetterandroidviews.adapters.interfaces

import timber.log.Timber

interface MultiChoiceAdapter<T: Selectable>: BaseAdapter {

	/**
	 * Max number of selected items allowed.
	 * Less than 0 means unlimited selected items are allowed.
	 */
	fun maxNumberOfSelectedItemsAllowed(): Int = -1
	/**
	 * Items that are currently selected.
	 */
	val currentlySelected: MutableList<T>
	
	fun onItemSelected(item: T, allowDeselect: Boolean = true) {
		if(!item.selectable) { //If not selectable, reassign as unselected and move on
			Timber.d("item not selectable | $item")
			item.selected = false
			return
		}
		if(currentlySelected.contains(item)) { //If already selected
			Timber.d("item already selected | $item")
			if(allowDeselect) { //And deselect is allowed
				//Deselect
				item.selected = false
				currentlySelected.remove(item)
			}
		}else if(currentlySelected.size<maxNumberOfSelectedItemsAllowed() || maxNumberOfSelectedItemsAllowed()<0) { //If not selected and we are not at the max number of allowed items
			Timber.d("item not selected | $item")
			//Select
			item.selected = true
			currentlySelected.add(item)
		} else if(currentlySelected.size==1) { //When max is one item, substitute one for other
			currentlySelected.getOrNull(0)?.let {
				it.selected = false
				updateItem(it)
			}
			currentlySelected.clear()
			item.selected = true
			currentlySelected.add(item)
		}
		//Update item with new select state
		updateItem(item)
	}

	/**
	 * Update item as needed
	 */
	fun updateItem(item: T)
	
}
