package com.inlacou.inkbetterandroidviews.dialogs.simple

import android.text.SpannableStringBuilder
import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class SimpleDialogMdl(
	override var title: SpannableStringBuilder? = null,
	override var buttonCancelText: SpannableStringBuilder? = null,
	override var buttonAcceptText: SpannableStringBuilder? = null,
	override var backgroundColorResId: Int? = null,
	var content: SpannableStringBuilder? = null,
	override var cancelOnOutsideClick: Boolean = true,
	var showAcceptButton: Boolean = true,
	var onAccepted: (() -> Unit)? = null,
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(title, buttonCancelText, buttonAcceptText, cancelOnOutsideClick, onCancelled, backgroundColorResId) {
	/**
	 * Constructor from [String] instead of [SpannableStringBuilder]
	 */
	constructor(
		title: String?,
		buttonCancelText: String? = null,
		buttonAcceptText: String? = null,
		backgroundColorResId: Int? = null,
		content: String,
		cancelOnOutsideClick: Boolean = true,
		showAcceptButton: Boolean = true,
		onAccepted: (() -> Unit),
		onCancelled: (() -> Unit)? = null
	): this(
		title?.let { SpannableStringBuilder(it) },
		buttonCancelText?.let { SpannableStringBuilder(it) },
		buttonAcceptText?.let { SpannableStringBuilder(it) },
		backgroundColorResId,
		SpannableStringBuilder(content),
		cancelOnOutsideClick,
		showAcceptButton,
		onAccepted,
		onCancelled,
	)
}