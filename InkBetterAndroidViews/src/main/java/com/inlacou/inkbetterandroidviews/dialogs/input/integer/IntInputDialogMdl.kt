package com.inlacou.inkbetterandroidviews.dialogs.input.integer

import android.text.SpannableStringBuilder
import com.inlacou.inkandroidextensions.ResId
import com.inlacou.inkbasicmodels.TextInputLayoutMdl
import com.inlacou.inkbasicmodels.TextStyleMdl
import com.inlacou.inkbasicmodels.TextViewMdl
import com.inlacou.inkbetterandroidviews.R
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class IntInputDialogMdl(
	override var title: TextViewMdl? = null,
	override var buttonCancelText: SpannableStringBuilder? = null,
	override var buttonAcceptText: SpannableStringBuilder? = null,
	override var backgroundColorResId: ResId? = null,
	var content: TextViewMdl? = null,
	var input: Int? = null,
	var hintPrefixSuffix: TextInputLayoutMdl? = null,
	var maxDigits: Int? = null,
	var required: Boolean = true,
	override var cancelOnOutsideClick: Boolean = true,
	var onAccepted: ((Int) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(
	title = title,
	buttonCancelText = buttonCancelText,
	buttonAcceptText = buttonAcceptText,
	cancelOnOutsideClick = cancelOnOutsideClick,
	onCancelled = onCancelled,
	backgroundColorResId = backgroundColorResId,
)