package com.inlacou.inkandroidextensions.view

import android.view.View
import android.widget.ScrollView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

fun ScrollView.scrollToBottom(delayMillis: Long = 300, doOnNext: (() -> Unit)? = null): Disposable {
	return Observable.timer(delayMillis, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
		this.fullScroll(View.FOCUS_DOWN)
		doOnNext?.invoke()
	}
}
