package com.inlacou.inkbetterandroidviews.dialogs.list.complex

import android.text.SpannableStringBuilder
import android.view.View
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogViewMdl

data class ComplexListDialogViewMdl<CustomView: View, CustomModel>(
	override var title: SpannableStringBuilder?,
	var itemLayoutResId: Int,
	var items: List<CustomModel>,
	override var cancelOnOutsideClick: Boolean = true,
	var onViewPopulate: ((ComplexListDialogView<CustomView, CustomModel>, CustomView, CustomModel) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogViewMdl(title, cancelOnOutsideClick, false, null, onCancelled)