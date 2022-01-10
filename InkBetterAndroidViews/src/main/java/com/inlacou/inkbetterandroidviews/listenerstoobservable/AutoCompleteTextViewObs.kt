package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.widget.AutoCompleteTextView
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class AutoCompleteTextViewObs constructor(private val betterSpinner: AutoCompleteTextView) : ObservableOnSubscribe<Pair<Int, Any>> {
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Pair<Int, Any>>) {
		betterSpinner.setOnItemClickListener { adapterView, view, i, l -> subscriber.onNext(Pair(i, adapterView.getItemAtPosition(i))) }
		subscriber.setCancellable { betterSpinner.onItemClickListener = null }
	}
}