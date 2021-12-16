package com.inlacou.inkbetterandroidviews.dialogs.list.complex

import android.text.SpannableStringBuilder
import android.view.View
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

data class ComplexListDialogMdl<CustomView: View, CustomModel>(
	override var title: SpannableStringBuilder?,
	var itemLayoutResId: Int,
	var items: List<CustomModel>,
	override var cancelOnOutsideClick: Boolean = true,
	var onViewPopulate: ((ComplexListDialog<CustomView, CustomModel>, CustomView, CustomModel) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(title, cancelOnOutsideClick, onCancelled)