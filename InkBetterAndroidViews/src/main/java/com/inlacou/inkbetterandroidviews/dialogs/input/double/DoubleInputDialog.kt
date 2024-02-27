package com.inlacou.inkbetterandroidviews.dialogs.input.double

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.inlacou.exinput.free.numeric.vdouble.DoubleInput
import com.inlacou.exinput.free.numeric.vint.IntInput
import com.inlacou.inkandroidextensions.getColorCompat
import com.inlacou.inkbetterandroidviews.databinding.DialogInputDoubleBinding
import com.inlacou.inkbetterandroidviews.databinding.DialogInputIntegerBinding
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialog

open class DoubleInputDialog @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	override val model: DoubleInputDialogMdl
) : BasicDialog(context, attrs, defStyleAttr) {

	private var binder: DialogInputDoubleBinding? = null
	override val shadow: View? get() = binder?.shadow
	override val dialog: View? get() = binder?.dialog
	override val tvTitle: TextView? get() = binder?.tvTitle
	open val tvContent: TextView? get() = binder?.tvContent
	open val eiDouble: DoubleInput? get() = binder?.eiDouble
	open val eiLayout: TextInputLayout? get() = binder?.eiLayout
	override val btnCancel: View? get() = binder?.btnCancel
	override val btnAccept: View? get() = binder?.btnAccept

	fun applyModel(newModel: DoubleInputDialogMdl) { //Copy contents
		model.content = newModel.content
		model.input = newModel.input
		model.hint = newModel.hint
		model.maxDigits = newModel.maxDigits
		model.required = newModel.required
		model.prefix = newModel.prefix
		model.suffix = newModel.suffix
		model.prefixColorResId = newModel.prefixColorResId
		model.suffixColorResId = newModel.suffixColorResId
		model.hintColorResId = newModel.hintColorResId
		super.applyModel(newModel)
	}

	private val controller: DoubleInputDialogCtrl by lazy { baseController as DoubleInputDialogCtrl }

	override fun initialize() {
		super.initialize()
		if(binder==null) binder = DialogInputDoubleBinding.inflate(LayoutInflater.from(context), this, true)
		baseController = DoubleInputDialogCtrl(view = this, model = model)
	}

	override fun populate() {
		super.populate()
		tvTitle?.text = model.title
		tvContent?.text = model.content
		eiDouble?.double = model.input
		eiLayout?.hint = model.hint
		eiLayout?.suffixText = model.suffix
		eiLayout?.prefixText = model.prefix
		tvTitle?.setTextColor(context.getColorCompat(model.titleColorResId))
		tvContent?.setTextColor(context.getColorCompat(model.contentColorResId))
		eiDouble?.setTextColor(context.getColorCompat(model.textColorResId))
		eiDouble?.setHintTextColor(context.getColorCompat(model.hintColorResId))
		eiLayout?.hintTextColor = ColorStateList.valueOf(context.getColorCompat(model.hintColorResId))
		eiLayout?.setSuffixTextColor(ColorStateList.valueOf(context.getColorCompat(model.suffixColorResId)))
		eiLayout?.setPrefixTextColor(ColorStateList.valueOf(context.getColorCompat(model.prefixColorResId)))
		eiDouble?.maxDigits = model.maxDigits
		eiDouble?.required = model.required
		eiDouble?.requestFocus()
	}

	override fun setListeners() {
		super.setListeners()
		btnAccept?.setOnClickListener { controller.onAcceptClick(eiDouble?.double) }
	}

}