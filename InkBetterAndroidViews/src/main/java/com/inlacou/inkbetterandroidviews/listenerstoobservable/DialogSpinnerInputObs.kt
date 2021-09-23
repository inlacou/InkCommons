package com.inlacou.inkbetterandroidviews.listenerstoobservable

import com.inlacou.exinput.free.spinner.SpinnerInput
import com.inlacou.exinput.free.spinner.dialog.DialogSpinnerInput
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class DialogSpinnerInputObs constructor(private val materialSpinner: DialogSpinnerInput) : ObservableOnSubscribe<Int> {
	
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Int>) {
		materialSpinner.onItemSelectedListener = object : SpinnerInput.OnItemSelectedListener {
			override fun onItemSelected(parent: SpinnerInput, item: Any?, position: Int) { subscriber.onNext(position) }
			override fun onNothingSelected(parent: SpinnerInput) {}
		}
		subscriber.setCancellable { materialSpinner.onItemSelectedListener = null }
	}

}