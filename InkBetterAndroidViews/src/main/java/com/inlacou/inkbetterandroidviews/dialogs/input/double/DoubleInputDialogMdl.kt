package com.inlacou.inkbetterandroidviews.dialogs.input.double

import android.text.SpannableStringBuilder
import com.inlacou.inkbasicmodels.TextStyleMdl
import com.inlacou.inkbasicmodels.TextViewMdl
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl
import com.inlacou.inkandroidextensions.ResId
import com.inlacou.inkbasicmodels.TextInputLayoutMdl

open class DoubleInputDialogMdl(
	override var title: TextViewMdl? = null,
	override var buttonCancelText: SpannableStringBuilder? = null,
	override var buttonAcceptText: SpannableStringBuilder? = null,
	override var backgroundColorResId: ResId? = null,
	var content: TextViewMdl? = null,
	var hintPrefixSuffix: TextInputLayoutMdl? = null,
	var input: Double? = null,
	var inputStyle: TextStyleMdl? = null,
	var maxDecimals: Int? = null,
	var maxDigits: Int? = null,
	var required: Boolean = true,
	override var cancelOnOutsideClick: Boolean = true,
	var onAccepted: ((Double) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(
	title = title,
	buttonCancelText = buttonCancelText,
	buttonAcceptText = buttonAcceptText,
	cancelOnOutsideClick = cancelOnOutsideClick,
	onCancelled = onCancelled,
	backgroundColorResId = backgroundColorResId,
)