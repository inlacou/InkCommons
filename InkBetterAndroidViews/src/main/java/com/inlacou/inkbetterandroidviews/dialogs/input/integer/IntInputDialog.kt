package com.inlacou.inkbetterandroidviews.dialogs.input.integer

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.inlacou.exinput.free.numeric.vint.IntInput
import com.inlacou.inkandroidextensions.getColorCompat
import com.inlacou.inkbasicmodels.TextViewMdl
import com.inlacou.inkbasicmodels.extensions.applyModel
import com.inlacou.inkbetterandroidviews.databinding.DialogInputIntegerBinding
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialog
import com.inlacou.pripple.RippleButton

open class IntInputDialog @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	override val model: IntInputDialogMdl
) : BasicDialog(context, attrs, defStyleAttr) {

	private var binder: DialogInputIntegerBinding? = null
	override val shadow: View? get() = binder?.shadow
	override val dialog: View? get() = binder?.dialog
	override val tvTitle: TextView? get() = binder?.tvTitle
	open val tvContent: TextView? get() = binder?.tvContent
	open val eiInt: IntInput? get() = binder?.eiInt
	open val eiLayout: TextInputLayout? get() = binder?.eiLayout
	override val btnCancel: RippleButton? get() = binder?.btnCancel
	override val btnAccept: RippleButton? get() = binder?.btnAccept

	fun applyModel(newModel: IntInputDialogMdl) { // Copy contents
		model.content = newModel.content
		model.input = newModel.input
		model.hintPrefixSuffix = newModel.hintPrefixSuffix
		model.maxDigits = newModel.maxDigits
		model.required = newModel.required
		super.applyModel(newModel)
	}

	private val controller: IntInputDialogCtrl by lazy { baseController as IntInputDialogCtrl }

	override fun initialize() {
		super.initialize()
		if(binder==null) binder = DialogInputIntegerBinding.inflate(LayoutInflater.from(context), this, true)
		baseController = IntInputDialogCtrl(view = this, model = model)
	}


	override fun populate() {
		super.populate()
		tvTitle?.applyModel(model.title)
		tvContent?.applyModel(model.content)
		eiInt?.int = model.input
		eiLayout?.applyModel(model.hintPrefixSuffix)
		eiInt?.maxDigits = model.maxDigits
		eiInt?.required = model.required
		eiInt?.requestFocus()
	}

	override fun setListeners() {
		super.setListeners()
		btnAccept?.setOnClickListener { controller.onAcceptClick(eiInt?.int) }
	}

}