package com.inlacou.inkbetterandroidviews.dialogs.list.simple

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

data class SimpleListDialogMdl(
	override var title: SpannableStringBuilder?,
	override var backgroundColorResId: Int? = null,
	var items: List<SimpleRvAdapter.Row>,
	override var cancelOnOutsideClick: Boolean = true,
	var onItemSelected: ((SimpleRvAdapter.Row) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(title, cancelOnOutsideClick, onCancelled, backgroundColorResId) {
	constructor(
		title: String,
		backgroundColorResId: Int? = null,
		items: List<SimpleRvAdapter.Row>,
		cancelOnOutsideClick: Boolean = true,
		onItemSelected: ((SimpleRvAdapter.Row) -> Unit),
		onCancelled: (() -> Unit)? = null
	): this(SpannableStringBuilder(title), backgroundColorResId, items, cancelOnOutsideClick, onItemSelected, onCancelled)
}