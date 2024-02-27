package com.inlacou.inkbetterandroidviews.dialogs.list.complex

import android.view.View
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogCtrl

open class ComplexListDialogCtrl<CustomView: View, CustomModel>
	(override val view: ComplexListDialog<CustomView, CustomModel>, override val model: ComplexListDialogMdl<CustomView, CustomModel>)
	: BasicDialogCtrl(view, model) {

}