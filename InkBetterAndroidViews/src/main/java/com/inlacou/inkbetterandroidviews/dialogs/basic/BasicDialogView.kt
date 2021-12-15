package com.inlacou.inkbetterandroidviews.dialogs.basic

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.inlacou.inkandroidextensions.view.setVisible
import com.inlacou.inkbetterandroidviews.dialogs.BaseDialog

abstract class BasicDialogView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
): BaseDialog(context, attrs, defStyleAttr) {

	abstract val shadow: View?
	abstract val dialog: View?
	abstract val tvTitle: TextView?
	abstract val btnCancel: View?
	abstract val btnAccept: View?

	abstract val model: BasicDialogViewMdl
	fun applyModel(newModel: BasicDialogViewMdl) { //Copy contents
		model.title = newModel.title
		model.cancelOnOutsideClick = newModel.cancelOnOutsideClick
		model.onAccepted = newModel.onAccepted
		model.onCancelled = newModel.onCancelled
		disposeAll()
		setListeners()
		populate()
	}

	private val controller: BasicDialogViewCtrl by lazy { baseController as BasicDialogViewCtrl }

	override fun onAttachedToWindow() {
		super.onAttachedToWindow()
		disposeAll()
		initialize()
		setListeners()
		populate()
	}

	open fun populate() {
		tvTitle?.text = model.title
		btnAccept?.setVisible(model.showAcceptButton, holdSpaceOnDisappear = false)
	}

	protected open fun setListeners() {
		shadow?.setOnClickListener { controller.onOutsideClick() }
		dialog?.setOnClickListener { /*Do nothing*/ }
		btnAccept?.setOnClickListener { controller.onAcceptClick() }
		btnCancel?.setOnClickListener { controller.onCancelClick() }
	}

}