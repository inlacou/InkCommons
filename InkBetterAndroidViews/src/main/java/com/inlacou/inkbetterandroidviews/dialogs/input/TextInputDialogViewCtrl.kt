package com.inlacou.inkbetterandroidviews.dialogs.input

import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogViewCtrl

class TextInputDialogViewCtrl(override val view: TextInputDialogView, override val model: TextInputDialogViewMdl): BasicDialogViewCtrl(view, model) {

	fun onAcceptClick(text: String) {
		if(view.eiText?.isValid()==true) {
			model.onAccepted.invoke(text)
			view.dismiss()
		}
	}

}