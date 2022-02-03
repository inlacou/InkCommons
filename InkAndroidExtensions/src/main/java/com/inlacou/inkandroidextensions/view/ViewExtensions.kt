package com.inlacou.inkandroidextensions.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.inlacou.inkandroidextensions.saveImage
import com.inlacou.inkandroidextensions.toUi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun View?.snackbar(messageResId: Int, length: Int = Snackbar.LENGTH_LONG) {
	this?.let { this.snackbar(this.context.getString(messageResId), length) }
}

fun View.onDrawn(callback: () -> Unit) {
	val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
		override fun onGlobalLayout() {
			viewTreeObserver?.removeOnGlobalLayoutListener(this)
			callback.invoke()
		}
	}
	viewTreeObserver?.addOnGlobalLayoutListener(listener)
}

val ViewGroup.childViews get() = (0..childCount).map { getChildAt(it) }

/**
 * Works on changes from:
 * GONE to VISIBLE
 * GONE to INVISIBLE
 * VISIBLE to GONE
 * INVISIBLE to GONE
 */
fun View.addOnVisibilityChange(callback: (Int) -> Unit): ViewTreeObserver.OnGlobalLayoutListener {
	val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
		var lastVisibility: Int? = null
		override fun onGlobalLayout() {
			val newVisibility = visibility
			if(lastVisibility!=newVisibility) callback.invoke(newVisibility)
			lastVisibility = newVisibility
		}
	}
	viewTreeObserver?.addOnGlobalLayoutListener(listener)
	return listener
}

fun View.shareScreenshot(activity: Activity, onSuccess: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null): Disposable? {
	this.getScreenshotBitmap().let {
		if(it!=null) {
			return activity.saveImage(it) {
				if(it!=null){
					shareImageUri(activity, it)
					onSuccess?.invoke()
				}else{
					onError?.invoke(Exception("Error: bitmap saving to app cache failed"))
				}
			}
		}
		else onError?.invoke(Exception("Error: view to bitmap transformation failed"))
	}
	return null
}

fun View.resizeView(width: Int, height: Int) {
	val lp = layoutParams
	if (width > -1) {
		lp.width = width
	}
	if (height > -1) {
		lp.height = height
	}
	layoutParams = lp
	requestLayout()
}

fun View.getCoordinates(): Rect {
	val offsetViewBounds = Rect()
	//returns the visible bounds
	getDrawingRect(offsetViewBounds)
	// calculates the relative coordinates to the parent
	parent?.let {
		if (it is ViewGroup) it.offsetDescendantRectToMyCoords(this, offsetViewBounds)
	}
	return offsetViewBounds
}

fun View?.snackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
	this?.let { Snackbar.make(it, message, length).show() }
}

fun View?.setVisible(visible: Boolean, holdSpaceOnDisappear: Boolean = false) {
	if (this == null) return
	if(visible){
		this.visibility = View.VISIBLE
	}else{
		if(holdSpaceOnDisappear){
			this.visibility = View.INVISIBLE
		}else{
			this.visibility = View.GONE
		}
	}
}

fun View.setMargins(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
	if (layoutParams is ViewGroup.MarginLayoutParams) {
		val p = layoutParams as ViewGroup.MarginLayoutParams
		p.setMargins(left ?: p.leftMargin, top ?: p.topMargin, right ?: p.rightMargin, bottom ?: p.bottomMargin)
		requestLayout()
	}
}

fun View.setPaddings(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
	setPadding(left ?: paddingLeft, top ?: paddingTop, right ?: paddingRight, bottom ?: paddingBottom)
	requestLayout()
}

fun View.setScrollingBehaviour() {
	val params = layoutParams as CoordinatorLayout.LayoutParams
	params.behavior = AppBarLayout.ScrollingViewBehavior()
	requestLayout()
}

fun View.eraseBehaviours() {
	val params = layoutParams as CoordinatorLayout.LayoutParams
	params.behavior = null
	requestLayout()
}

fun View.getScreenshotBitmap(): Bitmap? {
	Timber.d("getScreenshotBitmap")
	this.clearFocus()
	this.isPressed = false

	val willNotCache = this.willNotCacheDrawing()
	this.setWillNotCacheDrawing(false)

	// Reset the drawing cache background color to fully transparent
	// for the duration of this operation
	val color = this.drawingCacheBackgroundColor
	this.drawingCacheBackgroundColor = 0

	if (color != 0) {
		this.destroyDrawingCache()
	}
	this.buildDrawingCache()
	val cacheBitmap = this.drawingCache
	if (cacheBitmap == null) {
		Timber.e("failed getViewBitmap($this)")
		return null
	}

	val bitmap = Bitmap.createBitmap(cacheBitmap)

	// Restore the view
	this.destroyDrawingCache()
	this.setWillNotCacheDrawing(willNotCache)
	this.drawingCacheBackgroundColor = color

	Timber.d("returning")
	return bitmap
}

fun View.showOverflow(menuResId: Int, onMenuItemClick: (menuItem: MenuItem) -> Boolean) {
	val popup = PopupMenu(context, this)
	val inflater = popup.menuInflater
	inflater.inflate(menuResId, popup.menu)
	popup.setOnMenuItemClickListener {
		onMenuItemClick.invoke(it)
	}
	popup.show()
}

//Get out of here

/**
 * Shares the PNG image from Uri.
 * @param uri Uri of image to share.
 */
private fun shareImageUri(context: Context, uri: Uri) {
	val intent = Intent(Intent.ACTION_SEND)
	intent.putExtra(Intent.EXTRA_STREAM, uri)
	intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
	intent.type = "image/png"
	context.startActivity(intent)
}


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.cancelTransition() {
	transitionName = null
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)

fun CollapsingToolbarLayout.disableCollapse() {
	(layoutParams as AppBarLayout.LayoutParams?)?.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
}

fun CollapsingToolbarLayout.allowCollapseExitUntilCollapse() {
	(layoutParams as AppBarLayout.LayoutParams?)?.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
}

fun CollapsingToolbarLayout.allowCollapseEnterAlways() {
	(layoutParams as AppBarLayout.LayoutParams?)?.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
}

internal fun View.setBackgroundCompat(drawable: Drawable) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		this.background = drawable
	}else{
		this.setBackgroundDrawable(drawable)
	}
}

internal fun View.centerHorizontal() {
	layoutParams?.let { layoutParams ->
		if(layoutParams is RelativeLayout.LayoutParams){
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 0)
		}
	}
}

internal fun View.centerVertical() {
	layoutParams?.let { layoutParams ->
		if(layoutParams is RelativeLayout.LayoutParams){
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0)
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
		}
	}
}

internal fun View.center() {
	layoutParams.let {
		layoutParams = (it ?: RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)).apply {
			if(this is RelativeLayout.LayoutParams){
				this.addRule(RelativeLayout.CENTER_HORIZONTAL)
				this.addRule(RelativeLayout.CENTER_VERTICAL)
			}
		}
		
	}
}

internal fun View.alignParentTop() {
	layoutParams?.let { layoutParams ->
		if(layoutParams is RelativeLayout.LayoutParams){
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
		}
	}
}

internal fun View.alignParentBottom() {
	layoutParams?.let { layoutParams ->
		if(layoutParams is RelativeLayout.LayoutParams){
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
		}
	}
}

internal fun View.alignParentLeft() {
	layoutParams?.let { layoutParams ->
		if(layoutParams is RelativeLayout.LayoutParams){
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
		}
	}
}

internal fun View.alignParentRight() {
	layoutParams?.let { layoutParams ->
		if(layoutParams is RelativeLayout.LayoutParams) {
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
		}
	}
}

internal fun View.matchParent() {
	layoutParams?.let { layoutParams ->
		if(layoutParams is RelativeLayout.LayoutParams) {
			layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
			layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
		}
	}
}

fun View.showAlphaObs(durationMillis: Long): Observable<Float> {
	return Observable.intervalRange(0, 100, 0, (durationMillis/100), TimeUnit.MILLISECONDS).map { it.toFloat()/100 }.toUi().doOnNext{ factor ->
		alpha = factor
	}.doOnComplete {
		alpha = 1f /*Otherwise, on previous method factor can be limited to 0.99 instead*/
		setVisible(true, holdSpaceOnDisappear = true)
	}
}

fun View.hideAlphaObs(durationMillis: Long, holdSpaceOnDisappear: Boolean = true): Observable<Float> {
	return Observable.intervalRange(0, 100, 0, (durationMillis/100), TimeUnit.MILLISECONDS).map { 1f-(it.toFloat()/100) }.toUi().doOnNext{ factor ->
		alpha = factor
	}.doOnComplete {
		alpha = 0f /*To ensure it does not happen like in @see showAlphaObs*/
		setVisible(false, holdSpaceOnDisappear = holdSpaceOnDisappear)
	}
}

fun View.resizeObs(from: Float, to: Float, durationMillis: Long): Observable<Float> {
	return Observable.intervalRange(0, 100, 0, (durationMillis/100), TimeUnit.MILLISECONDS)
			.map { it.toFloat()/100 } //internal progress
			.map { from+((abs(to-from)*it)*(if(from<to) 1 else -1)) }
			.toUi().doOnNext{ factor ->
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED).let { measure(it, it) }
				resizeView((measuredWidth*factor).toInt(), (measuredHeight*factor).toInt())
				setVisible(true, holdSpaceOnDisappear = false)
			}
}

fun View.showResizeObs(durationMillis: Long): Observable<Float> {
	return resizeObs(0f, 1f, durationMillis)
}

fun View.hideResizeObs(durationMillis: Long): Observable<Float> {
	return resizeObs(1f, 0f, durationMillis)
}

/**
 * Works on changes from
 * GONE to VISIBLE
 * GONE to INVISIBLE
 * VISIBLE to GONE
 * INVISIBLE to GONE
 * @return (lambda) int representing VISIBLE, INVISIBLE, or GONE
 */
fun View.addOnVisibilityChangeListener(callback: (Int) -> Unit): ViewTreeObserver.OnGlobalLayoutListener {
	Timber.d("addOnVisibilityChange")
	val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
		var lastVisibility: Int? = null
		override fun onGlobalLayout() {
			val newVisibility = visibility
			if(lastVisibility!=newVisibility) callback.invoke(newVisibility)
			lastVisibility = newVisibility
		}
	}
	viewTreeObserver?.addOnGlobalLayoutListener(listener)
	return listener
}

fun View.removeOnVisibilityChangeListener(listener: ViewTreeObserver.OnGlobalLayoutListener?) {
	if(listener!=null) viewTreeObserver?.removeOnGlobalLayoutListener(listener)
}

fun View.resizeHeightObs(from: Float, to: Float, durationMillis: Long): Observable<Float> {
	return Observable.intervalRange(0, 100, 0, (durationMillis/100), TimeUnit.MILLISECONDS)
		.map { it.toFloat()/100 } //internal progress
		.map { from+((abs(to-from) *it)*(if(from<to) 1 else -1)) }
		.toUi().doOnNext{ factor ->
			View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED).let { measure(it, it) }
			resizeView(width, (measuredHeight*factor).toInt())
			setVisible(true, holdSpaceOnDisappear = false)
		}
}

fun View.resizeHeightObs(from: Int, to: Int, durationMillis: Long, log: Boolean = false): Observable<Float> {
	return Observable.intervalRange(0, 100, 0, (durationMillis/100), TimeUnit.MILLISECONDS)
		.map { it.toFloat()/100 } //internal progress
		.map { from+((abs(to-from)*it)*(if(from<to) 1 else -1)) }
		.toUi().doOnNext{ factor ->
			if(log) Timber.d("resize | factor: $factor")
			View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED).let { measure(it, it) }
			resizeView(width, factor.toInt())
			setVisible(true, holdSpaceOnDisappear = false)
		}
}

/**
 * Convert a view to bitmap
 */
fun View.toBitmap(): Bitmap {
	val displayMetrics = DisplayMetrics()
	(context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
	this.layoutParams = LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
	this.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
	this.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
	this.buildDrawingCache()
	val bitmap = Bitmap.createBitmap(this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)
	val canvas = Canvas(bitmap)
	this.draw(canvas)
	return bitmap
}

/**
 * For wrap content and match parent use ViewGroup.LayoutParams.WRAP_CONTENT or ViewGroup.LayoutParams.MATCH_PARENT.
 */
fun View.setWidthHeight(width: Int?, height: Int?) {
	var layoutParams = this.layoutParams
	when(layoutParams) {
		is RelativeLayout.LayoutParams -> { if(width!=null) layoutParams.width = width; if(height!=null) layoutParams.height = height }
		is LinearLayout.LayoutParams -> { if(width!=null) layoutParams.width = width; if(height!=null) layoutParams.height = height }
		is FrameLayout.LayoutParams -> { if(width!=null) layoutParams.width = width; if(height!=null) layoutParams.height = height }
		is CollapsingToolbarLayout.LayoutParams -> { if(width!=null) layoutParams.width = width; if(height!=null) layoutParams.height = height }
		is CoordinatorLayout.LayoutParams -> { if(width!=null) layoutParams.width = width; if(height!=null) layoutParams.height = height }
		is LinearLayoutCompat.LayoutParams -> { if(width!=null) layoutParams.width = width; if(height!=null) layoutParams.height = height }
		null -> { when(parent) {
			is RelativeLayout -> layoutParams = RelativeLayout.LayoutParams(width ?: ViewGroup.LayoutParams.WRAP_CONTENT, height ?: ViewGroup.LayoutParams.WRAP_CONTENT)
			is LinearLayout -> layoutParams = LinearLayout.LayoutParams(width ?: ViewGroup.LayoutParams.WRAP_CONTENT, height ?: ViewGroup.LayoutParams.WRAP_CONTENT)
			is FrameLayout -> layoutParams = FrameLayout.LayoutParams(width ?: ViewGroup.LayoutParams.WRAP_CONTENT, height ?: ViewGroup.LayoutParams.WRAP_CONTENT)
			is CollapsingToolbarLayout -> layoutParams = CollapsingToolbarLayout.LayoutParams(width ?: ViewGroup.LayoutParams.WRAP_CONTENT, height ?: ViewGroup.LayoutParams.WRAP_CONTENT)
			is CoordinatorLayout -> layoutParams = CoordinatorLayout.LayoutParams(width ?: ViewGroup.LayoutParams.WRAP_CONTENT, height ?: ViewGroup.LayoutParams.WRAP_CONTENT)
			is LinearLayoutCompat -> layoutParams = LinearLayoutCompat.LayoutParams(width ?: ViewGroup.LayoutParams.WRAP_CONTENT, height ?: ViewGroup.LayoutParams.WRAP_CONTENT)
			null -> layoutParams = LinearLayoutCompat.LayoutParams(width ?: ViewGroup.LayoutParams.WRAP_CONTENT, height ?: ViewGroup.LayoutParams.WRAP_CONTENT)
			else -> Timber.d("parent is ${parent.javaClass}")
		} }
	}
	Timber.d("new layoutParams: $layoutParams")
	this.layoutParams = layoutParams
}
