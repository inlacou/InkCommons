package com.inlacou.commons.ui.views.sidebar.row

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.inlacou.commons.R
import com.inlacou.commons.business.Section
import com.inlacou.commons.databinding.ViewSidebarRowBinding
import com.inlacou.inkandroidextensions.view.setDrawableRes
import com.inlacou.inkandroidextensions.view.setPaddings
import com.inlacou.commons.ui.views.sidebar.SidebarViewMdl
import com.inlacou.inkandroidextensions.dpToPx
import com.inlacou.inkandroidextensions.getColorCompat
import com.inlacou.inkbetterandroidviews.BaseView

open class SidebarRowView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
	: BaseView(context, attrs, defStyleAttr) {
	
	var binder: ViewSidebarRowBinding? = null
	val surfaceLayout: View? get() = binder?.viewBaseLayoutSurface
	val tvTitle: TextView? get() = binder?.title
	val ivIcon: ImageView? get() = binder?.iv

	var model: SidebarViewMdl = SidebarViewMdl(Section.values().first())
		set(value) {
			field = value
			controller.model = value
			populate()
		}
	private val controller: SidebarRowViewCtrl get() = baseController as SidebarRowViewCtrl

	init {
		this.initialize()
		baseController = SidebarRowViewCtrl(view = this, model = model)
		setListeners()
		populate()
	}

	override fun initialize() {
		super.initialize()
		binder = ViewSidebarRowBinding.inflate(LayoutInflater.from(context), this, true)
	}

	fun populate() {
		model.item.mdl.apply {
			surfaceLayout?.let { it.setPaddings(left = it.paddingLeft, top = paddingTop.dpToPx(), bottom = paddingBot.dpToPx(), right = it.paddingRight) }
			ivIcon?.setDrawableRes(iconResId ?: R.color.transparent)
			tvTitle?.text = textResId.let { if(it!=null) resources.getString(it) else "" }
			tvTitle?.setTextColor(resources.getColorCompat(if(model.selected) {
				R.color.red_text
			}else {
				R.color.text_over_white
			}))
			text?.let { tvTitle?.text = it }
		}
	}

	private fun setListeners() {
		surfaceLayout?.setOnClickListener { controller.onClick() }
	}

	fun inAnimation() {
		val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
		surfaceLayout?.startAnimation(animation)
	}
	
}