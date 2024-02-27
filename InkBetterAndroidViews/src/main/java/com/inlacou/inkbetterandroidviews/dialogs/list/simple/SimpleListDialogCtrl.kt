package com.inlacou.inkbetterandroidviews.dialogs.list.simple

import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogCtrl

open class SimpleListDialogCtrl(override val view: SimpleListDialog, override val model: SimpleListDialogMdl): BasicDialogCtrl(view, model) {

	open fun onItemSelected(item: SimpleRvAdapter.Row) {
		view.dismiss(onEnd = { model.onItemSelected.invoke(item) })
	}

}