package com.inlacou.inkbetterandroidviews.listenerstoobservable

import com.inlacou.exinput.free.spinner.SingleSpinnerInput
import com.inlacou.exinput.free.spinner.dialog.SingleDialogSpinnerInput
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class SingleDialogSpinnerInputObs constructor(private val materialSpinner: SingleDialogSpinnerInput) : ObservableOnSubscribe<Int> {
	
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Int>) {
		materialSpinner.onItemSelectedListener = object : SingleSpinnerInput.OnItemSelectedListener {
			override fun onItemSelected(parent: SingleSpinnerInput, item: Any?, position: Int) { subscriber.onNext(position) }
			override fun onNothingSelected(parent: SingleSpinnerInput) {}
		}
		subscriber.setCancellable { materialSpinner.onItemSelectedListener = null }
	}

}