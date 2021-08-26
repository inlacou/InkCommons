package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.widget.AutoCompleteTextView
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class AutoCompleteTextViewObs constructor(private val betterSpinner: AutoCompleteTextView) : ObservableOnSubscribe<Int> {
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Int>) {
		betterSpinner.setOnItemClickListener { adapterView, view, i, l -> subscriber.onNext(i) }
		subscriber.setCancellable { betterSpinner.onItemClickListener = null }
	}
}