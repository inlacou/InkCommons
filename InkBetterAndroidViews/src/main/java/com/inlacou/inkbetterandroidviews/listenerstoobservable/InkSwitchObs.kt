package com.inlacou.inkbetterandroidviews.listenerstoobservable

import com.inlacou.inkswitch.InkSwitch
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class InkSwitchObs constructor(private val view: InkSwitch) : ObservableOnSubscribe<Pair<Int, Boolean>> {
	
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Pair<Int, Boolean>>) {
		view.onValueSetListener = { index: Int, fromUser: Boolean ->
			subscriber.onNext(Pair(index, fromUser))
		}
	}

}