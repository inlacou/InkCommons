package com.inlacou.inkbetterandroidviews.adapters.interfaces

interface SingleChoiceAdapter<T: Selectable>: MultiChoiceAdapter<T> {
	override fun maxNumberOfSelectedItemsAllowed(): Int = 1
}
