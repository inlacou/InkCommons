package com.inlacou.inkandroidextensions.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

//RecyclerView

fun RecyclerView.scrollToWithDelay(position: Int = 0, delayMillis: Long = 300, doOnNext: (() -> Unit)? = null): Disposable {
	return Observable.timer(delayMillis, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
		this.scrollToPosition(position)
		doOnNext?.invoke()
	}
}

fun RecyclerView.hideOverScrollShadow() {
	overScrollMode = View.OVER_SCROLL_NEVER
}

fun RecyclerView.safeSmoothScrollToPosition(pos: Int, max: Int){
	smoothScrollToPosition(when {
		pos==max -> max-1
		pos<0 -> 0
		else -> pos
	})
}
///RecyclerView
