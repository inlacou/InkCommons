package com.inlacou.inkbetterandroidviews.listenerstoobservable

import com.inlacou.inkbetterandroidviews.spinners.BetterSpinner

import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class BetterSpinnerObs constructor(private val betterSpinner: BetterSpinner) : ObservableOnSubscribe<Int> {
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Int>) {
		betterSpinner.setOnItemClickListener { adapterView, view, i, l -> subscriber.onNext(i) }
		subscriber.setCancellable { betterSpinner.onItemClickListener = null }
	}
}