package com.inlacou.inkbetterandroidviews.spinners

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.inlacou.inkbetterandroidviews.R
import java.util.*

open class BetterSpinner: AppCompatAutoCompleteTextView {
	
	constructor(context: Context?) : super(context!!)
	constructor(arg0: Context?, arg1: AttributeSet?) : super(arg0!!, arg1)
	constructor(arg0: Context?, arg1: AttributeSet?, arg2: Int) : super(arg0!!, arg1, arg2)

	/**
	 * TODO when filtering it is hidden by the suggestions.
	 */
	var allowFilter: Boolean = false
	var clearOnClick: Boolean = false
	private var startClickTime: Long = 0
	private var isPopup = false
	
	init {
		reset()
		gravity = Gravity.CENTER_VERTICAL
		inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
	}
	
	override fun setText(text: CharSequence?, filter: Boolean) {
		super.setText(text, filter)
		onItemClick()
	}
	
	override fun setText(text: CharSequence?, type: BufferType?) {
		super.setText(text, type)
		onItemClick()
	}
	
	override fun enoughToFilter(): Boolean {
		return true
	}
	
	override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect)
		if (focused) {
			if(!allowFilter) {
				performFiltering(if(clearOnClick) "" else this.text, 0)
				(this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.windowToken, 0)
				this.keyListener = null
			}else{
				performFiltering("", 0)
			}
			dismissDropDown()
			isPopup = false
		} else {
			isPopup = false
		}
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean {
		return if (!this.isEnabled) {
			false
		} else {
			when (event.action) {
				0 -> startClickTime = Calendar.getInstance().timeInMillis
				1 -> {
					val clickDuration = Calendar.getInstance().timeInMillis - startClickTime
					if (clickDuration < MAX_CLICK_DURATION) {
						isPopup = if (isPopup) {
							dismissDropDown()
							false
						} else {
							this.requestFocus()
							showDropDown()
							true
						}
					}
				}
			}
			super.onTouchEvent(event)
		}
	}
	
	private fun onItemClick() {
		isPopup = false
	}

	/**
	 * Utility function
	 */
	fun setCompoundDrawablesWithIntrinsicBounds() = setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

	/**
	 * Used to set drawables for elements
	 */
	override fun setCompoundDrawablesWithIntrinsicBounds(
		left: Drawable?,
		top: Drawable?,
		right: Drawable?,
		bottom: Drawable?,
	) {
		var newRight = right
		val dropdownIcon = ContextCompat.getDrawable(this.context, R.mipmap.ic_arrow_drop_down)
		if (dropdownIcon != null) {
			newRight = dropdownIcon
			dropdownIcon.mutate().alpha = 128
		}
		super.setCompoundDrawablesWithIntrinsicBounds(left, top, newRight, bottom)
	}

	/**
	 * Reset view state
	 */
	fun reset() {
		setSimpleAdapter(listOf())
		super.setOnDismissListener { clearFocus() }
		setText("")
		replaceText("")
		requestLayout()
	}
	
	fun setSimpleAdapter(titles: List<String>) {
		setAdapter(ArrayAdapter(context, R.layout.common_simple_list_item, titles))
	}

	private var complexItems: List<ComplexItem>? = null

	fun setComplexAdapter(items: List<ComplexItem>) {
		complexItems = items
		setSimpleAdapter(items.map { it.display })
	}

	override fun performFiltering(text: CharSequence?, keyCode: Int) {
		complexItems.let { complexItems ->
			if(complexItems==null) super.performFiltering(text, keyCode)
			else setSimpleAdapter(complexItems.filter { it.filter(text.toString()) }.map { it.display })
		}
	}

	interface ComplexItem {
		val display: String
		fun filter(s: String): Boolean
	}

	override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter)
		try {
			(parent.parent as TextInputLayout).error = null
		}catch (e: Exception) {}
	}
	
	override fun setError(error: CharSequence?) {
		(parent.parent as TextInputLayout).error = error
	}
	
	companion object {
		private const val MAX_CLICK_DURATION = 200
	}
}