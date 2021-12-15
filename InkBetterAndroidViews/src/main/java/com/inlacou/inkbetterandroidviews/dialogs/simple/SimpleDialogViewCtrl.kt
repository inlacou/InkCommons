package com.inlacou.inkbetterandroidviews.dialogs.simple

import android.app.Activity
import com.inlacou.inkbetterandroidviews.BaseViewCtrl

class SimpleDialogViewCtrl(val view: SimpleDialogView, val model: SimpleDialogViewMdl): BaseViewCtrl(view, model) {

	fun onOutsideClick() = if(model.cancel) view.remove(view.context as Activity) else Unit

	fun onCancelClick() = view.remove(view.context as Activity)

	fun onAcceptClick() {
		//TODO
	}

}