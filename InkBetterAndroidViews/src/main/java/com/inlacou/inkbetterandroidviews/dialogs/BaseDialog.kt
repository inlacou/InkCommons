package com.inlacou.inkbetterandroidviews.dialogs

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.inlacou.inkbetterandroidviews.BaseView

abstract class BaseDialog @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
	: BaseView(context, attrs, defStyleAttr) {

	fun show(onEnd: (() -> Unit)? = null) = showAsDialog(context as Activity, onEnd)
	fun show(act: Activity, onEnd: (() -> Unit)? = null) = showAsDialog(act, onEnd)
	fun showAsDialog(act: Activity, onEnd: (() -> Unit)? = null) {
		(act.window.decorView as ViewGroup).addView(this)
		inAnimation(onEnd = onEnd)
	}

	fun dismiss(onEnd: (() -> Unit)? = null) = dismissAsDialog(context as Activity, onEnd)
	fun dismiss(act: Activity, onEnd: (() -> Unit)? = null) = dismissAsDialog(act, onEnd)
	fun dismissAsDialog(act: Activity, onEnd: (() -> Unit)? = null) {
		outAnimation(onEnd = { (act.window.decorView as ViewGroup).removeView(this); onEnd?.invoke() })
	}

	override val inAnimation: Int get() = android.R.anim.fade_in
	override val outAnimation: Int get() = android.R.anim.fade_out

}