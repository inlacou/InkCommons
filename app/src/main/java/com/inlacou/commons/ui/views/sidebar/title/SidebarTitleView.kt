package com.inlacou.commons.ui.views.sidebar.title

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.inlacou.commons.business.Section
import com.inlacou.commons.databinding.ViewSidebarTitleBinding
import com.inlacou.commons.ui.views.sidebar.SidebarViewMdl
import com.inlacou.inkbetterandroidviews.BaseView

open class SidebarTitleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
	: BaseView(context, attrs, defStyleAttr) {
	
	var binder: ViewSidebarTitleBinding? = null
	val surfaceLayout: View? get() = binder?.viewBaseLayoutSurface
	val tvTitle: TextView? get() = binder?.title

	var model: SidebarViewMdl = SidebarViewMdl(Section.values().first())
		set(value) {
			field = value
			controller.model = value
			populate()
		}
	private val controller: SidebarTitleViewCtrl get() = baseController as SidebarTitleViewCtrl

	init {
		this.initialize()
		baseController = SidebarTitleViewCtrl(view = this, model = model)
		setListeners()
		populate()
	}

	override fun initialize() {
		super.initialize()
		binder = ViewSidebarTitleBinding.inflate(LayoutInflater.from(context), this, true)
	}

	fun populate() {
		model.item.mdl.apply {
			tvTitle?.text = textResId.let { if(it!=null) resources.getString(it) else "" }
		}
		surfaceLayout?.isClickable = false
	}

	private fun setListeners() {
	}

	fun inAnimation() {
		val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
		surfaceLayout?.startAnimation(animation)
	}
	
}