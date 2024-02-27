package com.inlacou.inkbetterandroidviews.dialogs.basic

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.inlacou.inkbetterandroidviews.dialogs.BaseDialog

abstract class BasicDialog @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
): BaseDialog(context, attrs, defStyleAttr) {

	abstract val shadow: View?
	abstract val dialog: View?
	abstract val tvTitle: TextView?
	abstract val btnCancel: View?
	abstract val btnAccept: View?

	abstract val model: BasicDialogMdl
	fun applyModel(newModel: BasicDialogMdl) { //Copy contents
		model.title = newModel.title
		model.cancelOnOutsideClick = newModel.cancelOnOutsideClick
		model.onCancelled = newModel.onCancelled
		disposeAll()
		setListeners()
		populate()
	}

	private val controller: BasicDialogCtrl by lazy { baseController as BasicDialogCtrl }

	override fun onAttachedToWindow() {
		super.onAttachedToWindow()
		disposeAll()
		initialize()
		setListeners()
		populate()
	}

	open fun populate() {
		tvTitle?.text = model.title
		model.backgroundColorResId?.let { shadow?.setBackgroundResource(it) }
	}

	protected open fun setListeners() {
		shadow?.setOnClickListener { controller.onOutsideClick() }
		dialog?.setOnClickListener { /*Deliberately do nothing*/ }
		btnCancel?.setOnClickListener { controller.onCancelClick() }
	}

}