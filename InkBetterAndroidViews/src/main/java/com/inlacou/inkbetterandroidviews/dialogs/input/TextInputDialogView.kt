package com.inlacou.inkbetterandroidviews.dialogs.input

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.inlacou.exinput.free.text.TextInput
import com.inlacou.inkandroidextensions.getColorCompat
import com.inlacou.inkandroidextensions.view.tint
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
		model.prefix = newModel.prefix
		model.suffix = newModel.suffix
		model.prefixColorResId = newModel.prefixColorResId
		model.suffixColorResId = newModel.suffixColorResId
		model.hintColorResId = newModel.hintColorResId
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
		eiLayout?.suffixText = model.suffix
		eiLayout?.prefixText = model.prefix
		tvTitle?.setTextColor(context.getColorCompat(model.titleColorResId))
		tvContent?.setTextColor(context.getColorCompat(model.contentColorResId))
		eiText?.setTextColor(context.getColorCompat(model.textColorResId))
		eiText?.setHintTextColor(context.getColorCompat(model.hintColorResId))
		eiLayout?.hintTextColor = ColorStateList.valueOf(context.getColorCompat(model.hintColorResId))
		eiLayout?.setSuffixTextColor(ColorStateList.valueOf(context.getColorCompat(model.suffixColorResId)))
		eiLayout?.setPrefixTextColor(ColorStateList.valueOf(context.getColorCompat(model.prefixColorResId)))
		eiText?.minLength = model.minLength
		eiText?.required = model.required
		eiText?.requestFocus()
	}

	override fun setListeners() {
		super.setListeners()
		btnAccept?.setOnClickListener { controller.onAcceptClick(eiText?.text ?: "") }
	}

}