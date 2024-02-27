package com.inlacou.inkbetterandroidviews.dialogs.list.simple

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.databinding.DialogListSimpleBinding
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialog

open class SimpleListDialog @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	override val model: SimpleListDialogMdl
) : BasicDialog(context, attrs, defStyleAttr) {

	private var binder: DialogListSimpleBinding? = null
	override val shadow: View? get() = binder?.shadow
	override val dialog: View? get() = binder?.dialog
	override val tvTitle: TextView? get() = binder?.tvTitle
	open val rvContent: RecyclerView? get() = binder?.rvContent
	override val btnCancel: View? get() = binder?.btnCancel
	override val btnAccept: View? get() = binder?.btnAccept

	fun applyModel(newModel: SimpleListDialogMdl) { //Copy contents
		model.items = newModel.items
		super.applyModel(newModel)
	}

	private val controller: SimpleListDialogCtrl by lazy { baseController as SimpleListDialogCtrl }

	override fun initialize() {
		super.initialize()
		if(binder==null) binder = DialogListSimpleBinding.inflate(LayoutInflater.from(context), this, true)
		baseController = SimpleListDialogCtrl(view = this, model = model)
	}

	override fun populate() {
		super.populate()
		rvContent?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		rvContent?.adapter = SimpleRvAdapter(model.items) { controller.onItemSelected(it) }
	}

}