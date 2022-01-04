package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.widget.AutoCompleteTextView
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import timber.log.Timber

class AutoCompleteTextViewObs constructor(private val betterSpinner: AutoCompleteTextView) : ObservableOnSubscribe<Pair<Int, String>> {
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Pair<Int, String>>) {
		betterSpinner.setOnItemClickListener { adapterView, view, i, l -> subscriber.onNext(Pair(i, adapterView.getItemAtPosition(i).toString())) }
		subscriber.setCancellable { betterSpinner.onItemClickListener = null }
	}
}