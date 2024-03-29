package com.inlacou.inkbetterandroidviews.dialogs.timer

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class CountdownDialogMdl(
	override var title: SpannableStringBuilder? = null,
	override var buttonCancelText: SpannableStringBuilder? = null,
	override var buttonAcceptText: SpannableStringBuilder? = null,
	override var backgroundColorResId: Int? = null,
	open var time: Int,
	open var onTimerFinished: ((CountdownDialog) -> Unit)? = null,
	override var cancelOnOutsideClick: Boolean = true,
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(title, buttonCancelText, buttonAcceptText, cancelOnOutsideClick, onCancelled, backgroundColorResId)