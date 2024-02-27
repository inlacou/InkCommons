package com.inlacou.inkbetterandroidviews.dialogs.list.complex

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.inkbetterandroidviews.adapters.GenericRvAdapter
import com.inlacou.inkbetterandroidviews.databinding.DialogListSimpleBinding
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialog

open class ComplexListDialog<CustomView: View, CustomModel> @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	override val model: ComplexListDialogMdl<CustomView, CustomModel>
) : BasicDialog(context, attrs, defStyleAttr) {

	private var binder: DialogListSimpleBinding? = null
	override val shadow: View? get() = binder?.shadow
	override val dialog: View? get() = binder?.dialog
	override val tvTitle: TextView? get() = binder?.tvTitle
	open val rvContent: RecyclerView? get() = binder?.rvContent
	override val btnCancel: View? get() = binder?.btnCancel
	override val btnAccept: View? get() = binder?.btnAccept

	fun applyModel(newModel: ComplexListDialogMdl<CustomView, CustomModel>) { //Copy contents
		model.items = newModel.items
		super.applyModel(newModel)
	}

	private val controller: ComplexListDialogCtrl<CustomView, CustomModel> by lazy { baseController as ComplexListDialogCtrl<CustomView, CustomModel> }

	override fun initialize() {
		super.initialize()
		if(binder==null) binder = DialogListSimpleBinding.inflate(LayoutInflater.from(context), this, true)
		baseController = ComplexListDialogCtrl(view = this, model = model)
	}

	override fun populate() {
		super.populate()
		rvContent?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		rvContent?.adapter = GenericRvAdapter<CustomView, CustomModel>(itemList = model.items, layoutResourceId = model.itemLayoutResId, onViewPopulate = { customView, customModel ->
			model.onViewPopulate(this, customView, customModel)
		})
	}

}