package com.inlacou.inkbetterandroidviews.dialogs.simple

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.inlacou.inkbetterandroidviews.databinding.DialogSimpleBinding
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogView

class SimpleDialogView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	override val model: SimpleDialogViewMdl
) : BasicDialogView(context, attrs, defStyleAttr) {

	private var binder: DialogSimpleBinding? = null
	override val shadow: View? get() = binder?.shadow
	override val dialog: View? get() = binder?.dialog
	override val tvTitle: TextView? get() = binder?.tvTitle
	val tvContent: TextView? get() = binder?.tvContent
	override val btnCancel: View? get() = binder?.btnCancel
	override val btnAccept: View? get() = binder?.btnAccept

	fun applyModel(newModel: SimpleDialogViewMdl) { //Copy contents
		model.content = newModel.content
		model.onAccepted = newModel.onAccepted
		model.showAcceptButton = newModel.showAcceptButton
		super.applyModel(newModel)
	}

	private val controller: SimpleDialogViewCtrl by lazy { baseController as SimpleDialogViewCtrl }

	override fun initialize() {
		super.initialize()
		if(binder==null) binder = DialogSimpleBinding.inflate(LayoutInflater.from(context), this, true)
		baseController = SimpleDialogViewCtrl(view = this, model = model)
	}

	override fun populate() {
		super.populate()
		tvTitle?.text = model.title
		tvContent?.text = model.content
	}

	override fun setListeners() {
		super.setListeners()
	}

}