package com.inlacou.inkbetterandroidviews.dialogs.input.double

import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogCtrl

open class DoubleInputDialogCtrl(override val view: DoubleInputDialog, override val model: DoubleInputDialogMdl): BasicDialogCtrl(view, model) {

	open fun onAcceptClick(input: Double?) {
		if(view.eiDouble?.isValid()==true && input!=null) {
			view.dismiss(onEnd = { model.onAccepted.invoke(input) })
		}
	}

}