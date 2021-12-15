package com.inlacou.inkbetterandroidviews.dialogs

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.inlacou.inkbetterandroidviews.BaseView

abstract class BaseDialog @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
	: BaseView(context, attrs, defStyleAttr) {

	fun show(act: Activity) = showAsDialog(act)
	fun showAsDialog(act: Activity) {
		(act.window.decorView as ViewGroup).addView(this)
		inAnimation()
	}

	fun remove(act: Activity) = removeAsDialog(act)
	fun removeAsDialog(act: Activity) {
		outAnimation(onEnd = { (act.window.decorView as ViewGroup).removeView(this) })
	}

	override val inAnimation: Int get() = android.R.anim.fade_in
	override val outAnimation: Int get() = android.R.anim.fade_out

}