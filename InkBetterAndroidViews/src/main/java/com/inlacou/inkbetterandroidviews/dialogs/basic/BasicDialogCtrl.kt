package com.inlacou.inkbetterandroidviews.dialogs.basic

import android.app.Activity
import com.inlacou.inkbetterandroidviews.BaseViewCtrl

abstract class BasicDialogCtrl(open val view: BasicDialog, open val model: BasicDialogMdl): BaseViewCtrl(view, model) {

	open fun onOutsideClick() = if(model.cancelOnOutsideClick) {
		model.onCancelled?.invoke()
		view.dismiss()
	} else Unit

	open fun onCancelClick() = view.dismiss(view.context as Activity)

}