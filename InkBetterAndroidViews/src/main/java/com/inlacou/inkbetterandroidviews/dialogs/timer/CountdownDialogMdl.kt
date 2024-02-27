package com.inlacou.inkbetterandroidviews.dialogs.timer

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

data class CountdownDialogMdl(
	override var title: SpannableStringBuilder?,
	override var backgroundColorResId: Int? = null,
	var time: Int,
	var onTimerFinished: ((CountdownDialog) -> Unit)? = null,
	override var cancelOnOutsideClick: Boolean = true,
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(title, cancelOnOutsideClick, onCancelled, backgroundColorResId)