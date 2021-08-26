package com.inlacou.inkandroidextensions.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.*
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
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

fun View?.snackbar(messageResId: Int, length: Int = Snackbar.LENGTH_LONG){
	this?.let { this.snackbar(this.context.getString(messageResId), length) }
}

fun View.onDrawn(callback: () -> Unit){
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
 * Works on changes from
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

fun View?.snackbar(message: String, length: Int = Snackbar.LENGTH_LONG){
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

fun View.setScrollingBehaviour(){
	val params = layoutParams as CoordinatorLayout.LayoutParams
	params.behavior = AppBarLayout.ScrollingViewBehavior()
	requestLayout()
}

fun View.eraseBehaviours(){
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

fun View.showOverflow(menuResId: Int, onMenuItemClick: (menuItem: MenuItem) -> Boolean){
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

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
		LayoutInflater.from(context).inflate(layoutRes, this, false)


fun CollapsingToolbarLayout.disableCollapse(){
	(layoutParams as AppBarLayout.LayoutParams?)?.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
}

fun CollapsingToolbarLayout.allowCollapseExitUntilCollapse(){
	(layoutParams as AppBarLayout.LayoutParams?)?.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
}

fun CollapsingToolbarLayout.allowCollapseEnterAlways(){
	(layoutParams as AppBarLayout.LayoutParams?)?.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
}

internal fun View.setBackgroundCompat(drawable: Drawable){
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

internal fun View.centerVertical(){
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
		if(factor==0f) setVisible(true, holdSpaceOnDisappear = true)
	}
}

fun View.hideAlphaObs(durationMillis: Long): Observable<Float> {
	return Observable.intervalRange(0, 100, 0, (durationMillis/100), TimeUnit.MILLISECONDS).map { 1f-(it.toFloat()/100) }.toUi().doOnNext{ factor ->
		alpha = factor
		if(factor==0f) setVisible(false, holdSpaceOnDisappear = true)
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
