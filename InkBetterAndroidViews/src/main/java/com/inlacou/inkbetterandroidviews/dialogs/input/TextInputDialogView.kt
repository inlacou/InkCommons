package com.inlacou.inkbetterandroidviews.dialogs.input

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.inlacou.exinput.free.text.TextInput
import com.inlacou.inkbetterandroidviews.databinding.DialogInputTextBinding
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogView

class TextInputDialogView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	override val model: TextInputDialogViewMdl
) : BasicDialogView(context, attrs, defStyleAttr) {

	private var binder: DialogInputTextBinding? = null
	override val shadow: View? get() = binder?.shadow
	override val dialog: View? get() = binder?.dialog
	override val tvTitle: TextView? get() = binder?.tvTitle
	val tvContent: TextView? get() = binder?.tvContent
	val eiText: TextInput? get() = binder?.eiText
	val eiLayout: TextInputLayout? get() = binder?.eiLayout
	override val btnCancel: View? get() = binder?.btnCancel
	override val btnAccept: View? get() = binder?.btnAccept

	fun applyModel(newModel: TextInputDialogViewMdl) { //Copy contents
		model.content = newModel.content
		model.input = newModel.input
		model.hint = newModel.hint
		model.minLength = newModel.minLength
		model.required = newModel.required
		super.applyModel(newModel)
	}

	private val controller: TextInputDialogViewCtrl by lazy { baseController as TextInputDialogViewCtrl }

	override fun initialize() {
		super.initialize()
		if(binder==null) binder = DialogInputTextBinding.inflate(LayoutInflater.from(context), this, true)
		baseController = TextInputDialogViewCtrl(view = this, model = model)
	}

	override fun populate() {
		super.populate()
		tvTitle?.text = model.title
		tvContent?.text = model.content
		eiText?.text = model.input
		eiText?.hint = model.hint
		eiLayout?.hint = model.hint
		eiText?.minLength = model.minLength
		eiText?.required = model.required
		eiText?.requestFocus()
	}

	override fun setListeners() {
		super.setListeners()
		btnAccept?.setOnClickListener { controller.onAcceptClick(eiText?.text ?: "") }
	}

}