package com.inlacou.inkbetterandroidviews.checkedtextview

class CheckedTextViewCtrl(val view: CheckedTextView, var model: CheckedTextViewMdl) {

	init { //Initialize

	}

	fun onClick() {
		model.checked.not().let {
			if(!it && !model.uncheckeableOnClick){
				//uncheckeableOnClick ¯\_(ツ)_/¯
			}else{
				changeCheckedValue(it)
			}
		}
		model.clickListeners.forEach { it.invoke(view, model.checked) }
	}

	fun changeCheckedValue(value: Boolean) {
		model.checked = value
		model.checkedChangeListeners.forEach { it.invoke(view, value) }
		view.displayChecked(value)
	}
}