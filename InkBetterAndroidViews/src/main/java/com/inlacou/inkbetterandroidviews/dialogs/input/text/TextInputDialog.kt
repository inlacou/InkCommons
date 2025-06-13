package com.inlacou.inkbetterandroidviews.dialogs.input.text

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.inlacou.exinput.free.text.TextInput
import com.inlacou.inkbasicmodels.extensions.applyModel
import com.inlacou.inkbetterandroidviews.databinding.DialogInputTextBinding
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialog
import com.inlacou.pripple.RippleButton

open class TextInputDialog @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	override val model: TextInputDialogMdl
) : BasicDialog(context, attrs, defStyleAttr) {

	private var binder: DialogInputTextBinding? = null
	override val shadow: View? get() = binder?.shadow
	override val dialog: View? get() = binder?.dialog
	override val tvTitle: TextView? get() = binder?.tvTitle
	val tvContent: TextView? get() = binder?.tvContent
	val eiText: TextInput? get() = binder?.eiText
	val eiLayout: TextInputLayout? get() = binder?.eiLayout
	override val btnCancel: RippleButton? get() = binder?.btnCancel
	override val btnAccept: RippleButton? get() = binder?.btnAccept

	fun applyModel(newModel: TextInputDialogMdl) { //Copy contents
		model.content = newModel.content
		model.input = newModel.input
		model.inputLayout = newModel.inputLayout
		model.minLength = newModel.minLength
		model.required = newModel.required
		super.applyModel(newModel)
	}

	private val controller: TextInputDialogCtrl by lazy { baseController as TextInputDialogCtrl }

	override fun initialize() {
		super.initialize()
		if(binder==null) binder = DialogInputTextBinding.inflate(LayoutInflater.from(context), this, true)
		baseController = TextInputDialogCtrl(view = this, model = model)
	}

	override fun populate() {
		super.populate()
		tvTitle?.applyModel(model.title)
		tvContent?.applyModel(model.content)
		eiText?.text = model.input
		eiLayout?.applyModel(model.inputLayout)
		eiText?.minLength = model.minLength
		eiText?.required = model.required
		eiText?.requestFocus()
	}

	override fun setListeners() {
		super.setListeners()
		btnAccept?.setOnClickListener { controller.onAcceptClick(eiText?.text ?: "") }
	}

}