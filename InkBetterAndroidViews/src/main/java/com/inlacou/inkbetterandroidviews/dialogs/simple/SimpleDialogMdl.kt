package com.inlacou.inkbetterandroidviews.dialogs.simple

import android.text.SpannableStringBuilder
import com.inlacou.inkandroidextensions.ResId
import com.inlacou.inkbasicmodels.TextViewMdl
import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class SimpleDialogMdl(
	override var title: TextViewMdl? = null,
	override var buttonCancelText: SpannableStringBuilder? = null,
	override var buttonAcceptText: SpannableStringBuilder? = null,
	override var backgroundColorResId: ResId? = null,
	var content: TextViewMdl? = null,
	override var cancelOnOutsideClick: Boolean = true,
	var showAcceptButton: Boolean = true,
	var onAccepted: (() -> Unit)? = null,
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(
	title = title,
	buttonCancelText = buttonCancelText,
	buttonAcceptText = buttonAcceptText,
	cancelOnOutsideClick = cancelOnOutsideClick,
	onCancelled = onCancelled,
	backgroundColorResId = backgroundColorResId,
)