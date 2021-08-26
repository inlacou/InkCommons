package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.widget.CheckBox

import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class CheckBoxObs constructor(private val checkBox: CheckBox) : ObservableOnSubscribe<Boolean> {
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Boolean>) {
		checkBox.setOnCheckedChangeListener { buttonView, isChecked -> subscriber.onNext(isChecked) }
		subscriber.setCancellable { checkBox.setOnCheckedChangeListener(null) }
	}
}