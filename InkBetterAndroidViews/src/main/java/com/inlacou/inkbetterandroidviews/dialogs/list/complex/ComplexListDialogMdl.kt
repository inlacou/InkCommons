package com.inlacou.inkbetterandroidviews.dialogs.list.complex

import android.text.SpannableStringBuilder
import android.view.View
import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class ComplexListDialogMdl<CustomView: View, CustomModel>(
	override var title: SpannableStringBuilder?,
	override var backgroundColorResId: Int? = null,
	var itemLayoutResId: Int,
	var items: List<CustomModel>,
	override var cancelOnOutsideClick: Boolean = true,
	var onViewPopulate: ((ComplexListDialog<CustomView, CustomModel>, CustomView, CustomModel) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(title, cancelOnOutsideClick, onCancelled, backgroundColorResId) {
	constructor(
		title: String,
		backgroundColorResId: Int? = null,
		itemLayoutResId: Int,
		items: List<CustomModel>,
		cancelOnOutsideClick: Boolean = true,
		onViewPopulate: ((ComplexListDialog<CustomView, CustomModel>, CustomView, CustomModel) -> Unit),
		onCancelled: (() -> Unit)? = null
	): this(SpannableStringBuilder(title), backgroundColorResId, itemLayoutResId, items, cancelOnOutsideClick, onViewPopulate, onCancelled)
}