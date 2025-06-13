package com.inlacou.inkbetterandroidviews.dialogs.list.simple

import android.text.SpannableStringBuilder
import com.inlacou.inkandroidextensions.ResId
import com.inlacou.inkbasicmodels.TextViewMdl
import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class SimpleListDialogMdl(
	override var title: TextViewMdl? = null,
	override var buttonCancelText: SpannableStringBuilder? = null,
	override var buttonAcceptText: SpannableStringBuilder? = null,
	override var backgroundColorResId: ResId? = null,
	var items: List<SimpleRvAdapter.Row>,
	override var cancelOnOutsideClick: Boolean = true,
	var onItemSelected: ((SimpleRvAdapter.Row) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(
	title = title,
	buttonCancelText = buttonCancelText,
	buttonAcceptText = buttonAcceptText,
	cancelOnOutsideClick = cancelOnOutsideClick,
	onCancelled = onCancelled,
	backgroundColorResId = backgroundColorResId,
)