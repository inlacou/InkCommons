package com.inlacou.inkbetterandroidviews.spinners

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
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

	private var unfocusTimeMillis = 0L
	private var keyboardVisible = false

	/**
	 * Whether to allow filtering by inputting text.
	 */
	var allowFilter: Boolean = false
	/**
	 * Clear current filter on new click.
	 */
	var clearOnClick: Boolean = false
	/**
	 * Sets suggestion dropdown height.
	 * Used to avoid it from occupying the whole screen.
	 */
	var desiredDropdownHeight: Int? = null

	/**
	 * Variable to control time passed since first click.
	 */
	private var startClickTime: Long = 0
	/**
	 * Variable to control if popup is showing.
	 */
	private var isPopup = false

	/**
	 * Simple items, just strings.
	 */
	private var simpleItems: List<String>? = null

	/**
	 * Complex items, so you can filter by something other than display text if you want.
	 */
	private var complexItems: List<ComplexItem>? = null

	private var complexAdapterBuilder: ((items: List<ComplexItem>) -> ArrayAdapter<*>)? = null

	init {
		reset()
		gravity = Gravity.CENTER_VERTICAL
		isFocusable = true
		isFocusableInTouchMode = true
		inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
	}

	override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {
		return if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
			val keyboard = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			if (keyboardVisible) {
				keyboardVisible = false
				keyboard.hideSoftInputFromWindow(windowToken, 0)
				true
			} else if (isPopup) {
				dismissDropDown()
				isPopup = false
				clearFocus()
				true
			} else super.dispatchKeyEventPreIme(event)
		} else super.dispatchKeyEventPreIme(event)
	}

	override fun setText(text: CharSequence?, filter: Boolean) {
		super.setText(text, filter)
		onItemClick()
	}
	
	override fun setText(text: CharSequence?, type: BufferType?) {
		super.setText(text, type)
		onItemClick()
	}

	override fun setOnItemClickListener(listener: AdapterView.OnItemClickListener?) {
		if(listener==null) super.setOnItemClickListener(null)
		else super.setOnItemClickListener { adapterView: AdapterView<*>, view: View, position: Int, id: Long ->
			val item = adapterView.getItemAtPosition(position)
			val itemId = adapterView.getItemIdAtPosition(position)
			if(filter != null) {
				if(simpleItems?.contains(item)==true) listener.onItemClick(adapterView, view, simpleItems!!.indexOf(item), itemId)
				else if(complexItems?.map { it.display }?.contains(item)==true) listener.onItemClick(adapterView, view, complexItems!!.map { it.display }!!.indexOf(item), itemId)
				else listener.onItemClick(adapterView, view, position, id)
			} else listener.onItemClick(adapterView, view, position, id)
		}
	}

	override fun enoughToFilter(): Boolean = true

	override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
		if (focused) {
			keyboardVisible = true
			if(allowFilter) {
				val clear = clearOnClick && System.currentTimeMillis()-unfocusTimeMillis>200
				if(clear) setText("")
				performFiltering(if(clear) "" else this.text, 0)
				desiredDropdownHeight?.let { dropDownHeight = it }
			} else {
				performFiltering("", 0)
				(this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.windowToken, 0)
				this.keyListener = null
			}
			showDropDown()
			isPopup = true
		} else {
			unfocusTimeMillis = System.currentTimeMillis()
			isPopup = false
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect)
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean {
		return if (!this.isEnabled) {
			false
		} else {
			when (event.action) {
				MotionEvent.ACTION_DOWN -> startClickTime = Calendar.getInstance().timeInMillis
				MotionEvent.ACTION_UP -> {
					if ((Calendar.getInstance().timeInMillis - startClickTime) < MAX_CLICK_DURATION) {
						isPopup = if (isPopup) {
							dismissDropDown()
							false
						} else {
							this.requestFocus()
							// showDropDown()
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
	override fun setCompoundDrawablesWithIntrinsicBounds(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?, ) {
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
		setTitles(listOf())
		super.setOnDismissListener { clearFocus() }
		setText("")
		replaceText("")
		requestLayout()
	}
	
	fun setSimpleAdapter(titles: List<String>) {
		simpleItems = titles
		setTitles(titles)
	}

	private fun setTitles(titles: List<String>) {
		setAdapter(ArrayAdapter(context, R.layout.common_simple_list_item, titles))
	}

	private fun setComplexTitles(titles: List<ComplexItem>) {
		setAdapter(complexAdapterBuilder?.invoke(titles) ?: ArrayAdapter(context, R.layout.common_simple_list_item, titles.map { it.display }))
	}

	fun setComplexAdapter(items: List<ComplexItem>) {
		complexItems = items
		setComplexTitles(items)
	}

	fun setComplexAdapter(items: List<ComplexItem>, complexAdapterBuilder: (items: List<ComplexItem>) -> ArrayAdapter<*>) {
		complexItems = items
		this.complexAdapterBuilder = complexAdapterBuilder
		setComplexTitles(items)
	}

	override fun performFiltering(text: CharSequence?, keyCode: Int) {
		simpleItems.let { simpleItems ->
			complexItems.let { complexItems ->
				when {
					complexItems != null -> setComplexTitles(complexItems.filter { it.filter(text.toString()) })
					simpleItems != null -> setTitles(simpleItems.filter { it.contains(text.toString(), ignoreCase = true) })
					else -> {
						super.performFiltering(text, keyCode)
						//(adapter as ArrayAdapter<*>).filter.filter(text)
					}
				}
			}
		}
	}

	interface ComplexItem {
		val display: String
		fun filter(s: String): Boolean
	}

	override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter)
		(parent?.parent as? TextInputLayout)?.error = null
	}
	
	override fun setError(error: CharSequence?) {
		(parent?.parent as? TextInputLayout)?.error = error
	}
	
	companion object {
		private const val MAX_CLICK_DURATION = 200
	}
}