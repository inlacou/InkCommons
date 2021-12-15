package com.inlacou.inkbetterandroidviews

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

abstract class BaseView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
	: FrameLayout(context, attrs, defStyleAttr) {
	
	var disposables: MutableList<Disposable?> = mutableListOf()
	protected lateinit var baseController: BaseViewCtrl
	
	protected open fun initialize() = tryToObserveLifeCycle()

	private fun tryToObserveLifeCycle() {
		context.let {
			val lifecycleObserver = object: LifecycleObserver {
				@OnLifecycleEvent(Lifecycle.Event.ON_START) fun onStart() = this@BaseView.onStart()
				@OnLifecycleEvent(Lifecycle.Event.ON_RESUME) fun onResume() = this@BaseView.onResume()
				@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) fun onPause() =	this@BaseView.onPause()
				@OnLifecycleEvent(Lifecycle.Event.ON_STOP) fun onStop() = this@BaseView.onStop()
				@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) fun onDestroy() = this@BaseView.onDestroy()
			}
			tryToRegister(it, lifecycleObserver)
		}
	}
	
	private fun tryToRegister(it: Context?, lifecycleObserver: LifecycleObserver) {
		when (it) {
			is AppCompatActivity -> it.lifecycle.addObserver(lifecycleObserver)
			is Fragment -> it.lifecycle.addObserver(lifecycleObserver)
			is ContextWrapper -> tryToRegister(it.baseContext, lifecycleObserver)
			else -> Timber.w("not appCompatActivity, not fragment, not ContextWrapper: $it")
		}
	}
	
	open fun onStart() {
		Timber.d("onStart")
		try{ baseController.onStart() }catch (upe: UninitializedPropertyAccessException) {}
	}
	open fun onResume() {
		Timber.d("onResume")
		try{ baseController.onResume() }catch (upe: UninitializedPropertyAccessException) {}
	}
	open fun onPause() {
		Timber.d("onPause")
		baseController.onPause()
	}
	open fun onStop() {
		Timber.d("onStop")
		baseController.onStop()
	}
	open fun onDestroy() {
		Timber.d("onDestroy")
		disposeAll()
	}

	fun disposeAll() = disposables.forEach { it?.dispose() }

	/**
	 * Set in animation here as android.R.anim.XXXX or R.anim.XXXX
	 * For example: android.R.anim.fade_in
	 */
	open val inAnimation: Int? = null

	fun inAnimation(onStart: (() -> Unit)? = null, onEnd: (() -> Unit)? = null) {
		inAnimation.let {
			if(it==null) {
				onStart?.invoke()
				onEnd?.invoke()
				return
			}
			val animation = AnimationUtils.loadAnimation(context, it)
			startAnimation(animation)
			animation.setAnimationListener(object : Animation.AnimationListener {
				override fun onAnimationStart(animation: Animation) {
					onStart?.invoke()
				}

				override fun onAnimationEnd(animation: Animation) {
					onEnd?.invoke()
				}

				override fun onAnimationRepeat(animation: Animation) {}
			})
		}
	}

	/**
	 * Set out animation here as android.R.anim.XXXX or R.anim.XXXX
	 * For example: android.R.anim.fade_out
	 */
	open val outAnimation: Int? = null

	fun outAnimation(onStart: (() -> Unit)? = null, onEnd: (() -> Unit)? = null) {
		outAnimation.let {
			if(it==null) {
				onStart?.invoke()
				onEnd?.invoke()
				return
			}
			val animation = AnimationUtils.loadAnimation(context, it)
			startAnimation(animation)
			animation.setAnimationListener(object : Animation.AnimationListener {
				override fun onAnimationStart(animation: Animation) {
					onStart?.invoke()
				}

				override fun onAnimationEnd(animation: Animation) {
					onEnd?.invoke()
				}

				override fun onAnimationRepeat(animation: Animation) {}
			})
		}
	}
}