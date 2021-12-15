package com.inlacou.inkbetterandroidviews.dialogs.simple

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.inlacou.inkbetterandroidviews.databinding.ViewSimpleDialogBinding
import com.inlacou.inkbetterandroidviews.dialogs.BaseDialog
import timber.log.Timber

class SimpleDialogView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	val model: SimpleDialogViewMdl = SimpleDialogViewMdl(null)
) : BaseDialog(context, attrs, defStyleAttr) {

	private var binder: ViewSimpleDialogBinding? = null
	val viewBaseLayoutSurface: View? get() = binder?.viewBaseLayoutSurface
	val tvTitle: TextView? get() = binder?.tvTitle
	val tvContent: TextView? get() = binder?.tvContent
	val btnCancel: View? get() = binder?.btnCancel
	val btnAccept: View? get() = binder?.btnAccept

	fun applyModel(newModel: SimpleDialogViewMdl) { //Copy contents
		model.title = newModel.title
		model.content = newModel.content
		model.cancel = newModel.cancel
		model.onClick = newModel.onClick
		model.onDelete = newModel.onDelete
		disposeAll()
		setListeners()
		populate()
	}

	private val controller: SimpleDialogViewCtrl by lazy { baseController as SimpleDialogViewCtrl }

	override fun onAttachedToWindow() {
		super.onAttachedToWindow()
		disposeAll()
		initialize()
		setListeners()
		populate()
	}

	override fun initialize() {
		super.initialize()
		if(binder==null) binder = ViewSimpleDialogBinding.inflate(LayoutInflater.from(context), this, true)
		baseController = SimpleDialogViewCtrl(view = this, model = model)
		Timber.d("viewBaseLayoutSurface: $viewBaseLayoutSurface")
	}

	fun populate() {
		tvTitle?.text = model.title
		tvContent?.text = model.content
	}

	private fun setListeners() {
		viewBaseLayoutSurface?.setOnClickListener { controller.onOutsideClick() }
		btnAccept?.setOnClickListener { controller.onAcceptClick() }
		btnCancel?.setOnClickListener { controller.onCancelClick() }
	}

}