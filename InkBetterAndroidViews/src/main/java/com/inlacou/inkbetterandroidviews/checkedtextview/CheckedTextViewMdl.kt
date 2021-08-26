package com.inlacou.inkbetterandroidviews.checkedtextview

data class CheckedTextViewMdl(
	val item: Checkeable,
	var checked: Boolean,
	val checkedResId: Int,
	val uncheckedResId: Int,
	val uncheckeableOnClick: Boolean = false,
	val onClickListener: ((view: CheckedTextView, checked: Boolean) -> Unit)? = null,
	val onCheckedChangeListener: ((view: CheckedTextView, checked: Boolean) -> Unit)? = null){

	internal val checkedChangeListeners: MutableList<((view: CheckedTextView, item: Boolean) -> Unit)> = mutableListOf()
	internal val clickListeners: MutableList<((view: CheckedTextView, item: Boolean) -> Unit)> = mutableListOf()

	val text: String
	get() = item.displayName

	init {
		onCheckedChangeListener?.let { checkedChangeListeners.add(it) }
		onClickListener?.let { clickListeners.add(it) }
	}
}