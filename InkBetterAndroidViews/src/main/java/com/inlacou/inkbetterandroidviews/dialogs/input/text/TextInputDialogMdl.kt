package com.inlacou.inkbetterandroidviews.dialogs.input.text

import android.text.SpannableStringBuilder
import com.inlacou.exinput.ExInputConfig
import com.inlacou.inkandroidextensions.ResId
import com.inlacou.inkbasicmodels.TextInputLayoutMdl
import com.inlacou.inkbasicmodels.TextInputMdl
import com.inlacou.inkbasicmodels.TextViewMdl
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogMdl

open class TextInputDialogMdl(
	override var title: TextViewMdl? = null,
	override var buttonCancelText: SpannableStringBuilder? = null,
	override var buttonAcceptText: SpannableStringBuilder? = null,
	override var backgroundColorResId: ResId? = null,
	var content: TextViewMdl? = null,
	var inputLayout: TextInputLayoutMdl? = null,
	var input: String,
	var minLength: Int = 0,
	var required: Boolean = true,
	override var cancelOnOutsideClick: Boolean = true,
	var onAccepted: ((String) -> Unit),
	override var onCancelled: (() -> Unit)? = null,
): BasicDialogMdl(
	title = title,
	buttonCancelText = buttonCancelText,
	buttonAcceptText = buttonAcceptText,
	cancelOnOutsideClick = cancelOnOutsideClick,
	onCancelled = onCancelled,
	backgroundColorResId = backgroundColorResId,
)