package com.inlacou.inkbetterandroidviews.dialogs.timer

import android.text.SpannableStringBuilder
import com.inlacou.inkandroidextensions.ResId
import com.inlacou.inkbasicmodels.TextViewMdl
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class CountdownDialogMdl(
	override var title: TextViewMdl? = null,
	override var buttonCancelText: SpannableStringBuilder? = null,
	override var buttonAcceptText: SpannableStringBuilder? = null,
	override var backgroundColorResId: ResId? = null,
	open var time: Int,
	open var onTimerFinished: ((CountdownDialog) -> Unit)? = null,
	override var cancelOnOutsideClick: Boolean = true,
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(
	title = title,
	buttonCancelText = buttonCancelText,
	buttonAcceptText = buttonAcceptText,
	cancelOnOutsideClick = cancelOnOutsideClick,
	onCancelled = onCancelled,
	backgroundColorResId = backgroundColorResId,
)