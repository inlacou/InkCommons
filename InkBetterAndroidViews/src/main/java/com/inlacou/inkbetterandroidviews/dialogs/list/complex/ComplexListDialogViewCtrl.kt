package com.inlacou.inkbetterandroidviews.dialogs.list.complex

import android.view.View
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogViewCtrl
import com.inlacou.inkbetterandroidviews.dialogs.list.complex.ComplexListDialogView

class ComplexListDialogViewCtrl<CustomView: View, CustomModel>
	(override val view: ComplexListDialogView<CustomView, CustomModel>, override val model: ComplexListDialogViewMdl<CustomView, CustomModel>)
	: BasicDialogViewCtrl(view, model) {

}