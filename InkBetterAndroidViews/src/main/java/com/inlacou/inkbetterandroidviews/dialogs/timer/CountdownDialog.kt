package com.inlacou.inkbetterandroidviews.dialogs.timer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.inlacou.inkbetterandroidviews.databinding.DialogCountdownBinding
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialog
import com.inlacou.pripple.RippleButton

open class CountdownDialog @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	override val model: CountdownDialogMdl
) : BasicDialog(context, attrs, defStyleAttr) {

	private var binder: DialogCountdownBinding? = null
	override val shadow: View? get() = binder?.shadow
	override val dialog: View? get() = binder?.dialog
	override val tvTitle: TextView? get() = binder?.tvTitle
	protected val tvTime: TextView? get() = binder?.tvTime
	override val btnCancel: RippleButton? get() = binder?.btnCancel
	override val btnAccept: RippleButton? get() = binder?.btnStopResume

	open fun applyModel(newModel: CountdownDialogMdl) { //Copy contents
		model.time = newModel.time
		model.onTimerFinished = newModel.onTimerFinished
		super.applyModel(newModel)
	}

	private val controller: CountdownDialogCtrl by lazy { baseController as CountdownDialogCtrl }

	override fun initialize() {
		super.initialize()
		if(binder==null) binder = DialogCountdownBinding.inflate(LayoutInflater.from(context), this, true)
		baseController = CountdownDialogCtrl(view = this, model = model)
	}

	override fun onAttachedToWindow() {
		super.onAttachedToWindow()
		controller.start()
	}

	override fun setListeners() {
		super.setListeners()
		btnAccept?.setOnClickListener { controller.onStopResume() }
	}

	open fun setTimerText(s: String) {
		tvTime?.text = s
	}

}