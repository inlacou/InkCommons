package com.inlacou.inkbetterandroidviews.checkedtextview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.inlacou.inkandroidextensions.view.resizeObs
import com.inlacou.inkbetterandroidviews.R
import io.reactivex.rxjava3.disposables.Disposable

open class CheckedTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
	: FrameLayout(context, attrs, defStyleAttr) {

	//SHOULD add style attrs to TextView and both ImageViews

	var surfaceLayout: View? = null
	var tvText: TextView? = null
	var ivChecked: ImageView? = null
	var ivUnchecked: ImageView? = null

	private var resizeObs: Disposable? = null
	
	var model: CheckedTextViewMdl = CheckedTextViewMdl(object: Checkeable {
		override var displayName: String
			get() = ""
			set(value) {}
	}, false,
			R.color.transparent, R.color.transparent, true)
		set(value) {
			field = value
			controller.model = value
			populate()
		}
	val text: String
		get() = model.text
	var checked: Boolean
		get() = model.checked
		set(value) {
			if(model.checked!=value) { controller.changeCheckedValue(value) }
		}

	private lateinit var controller: CheckedTextViewCtrl

	init {
		this.initialize()
		setListeners()
		populate()
	}

	protected fun initialize() {
		val rootView = View.inflate(context, R.layout.view_checked_text_view, this)
		initialize(rootView)
	}

	fun initialize(view: View) {
		controller = CheckedTextViewCtrl(view = this, model = model)
		surfaceLayout = view.findViewById(R.id.view_base_layout_surface)
		tvText = view.findViewById(R.id.tv_text)
		ivChecked = view.findViewById(R.id.iv_checked)
		ivUnchecked = view.findViewById(R.id.iv_unchecked)
	}

	fun populate() {
		model.apply {
			tvText?.text = text
			ivChecked?.setImageResource(checkedResId)
			ivUnchecked?.setImageResource(uncheckedResId)
			displayChecked(checked, 0)
		}
	}

	internal fun displayChecked(checked: Boolean, duration: Long? = null) {
		//TODO add alternative for no RX
		resizeObs?.dispose()
		resizeObs = if(checked) {
			ivChecked?.resizeObs(0.5f, 1f, duration ?: 200)?.subscribe({},{},{})
		} else {
			ivChecked?.resizeObs(1f, 0.5f, duration ?: 200)?.subscribe({},{},{})
		}
	}
	
	private fun setListeners() {
		surfaceLayout?.setOnClickListener { controller.onClick() }
	}
	
	fun clearOnCheckedChangeListeners() {
		model.checkedChangeListeners.clear()
	}
	
	fun addOnCheckedChangeListener(listener: ((view: CheckedTextView, item: Boolean) -> Unit)) {
		model.checkedChangeListeners.add(listener)
	}

	fun removeOnCheckedChangeListener(listener: ((view: CheckedTextView, item: Boolean) -> Unit)) {
		model.checkedChangeListeners.remove(listener)
	}

	fun clearClickListeners() {
		model.clickListeners.clear()
	}

	fun addOnClickListener(listener: ((view: CheckedTextView, item: Boolean) -> Unit)) {
		model.clickListeners.add(listener)
	}

	fun removeOnClickListener(listener: ((view: CheckedTextView, item: Boolean) -> Unit)) {
		model.clickListeners.remove(listener)
	}

	fun inAnimation() {
		val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
		surfaceLayout?.startAnimation(animation)
	}
}