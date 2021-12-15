package com.inlacou.inkbetterandroidviews.dialogs.list.simple

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogViewMdl

data class SimpleListDialogViewMdl(
	override var title: SpannableStringBuilder?,
	var items: List<SimpleRvAdapter.Row>,
	override var cancelOnOutsideClick: Boolean = true,
	var onItemSelected: ((SimpleRvAdapter.Row) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogViewMdl(title, cancelOnOutsideClick, false, null, onCancelled)