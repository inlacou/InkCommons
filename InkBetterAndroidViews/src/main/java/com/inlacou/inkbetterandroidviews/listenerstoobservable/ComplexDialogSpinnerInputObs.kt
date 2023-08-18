package com.inlacou.inkbetterandroidviews.listenerstoobservable

import com.inlacou.exinput.free.spinner.ComplexSpinnerInput
import com.inlacou.exinput.free.spinner.dialog.ComplexDialogSpinnerInput
import com.inlacou.inkkotlincommons.monads.Maybe
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class ComplexDialogSpinnerInputObs<T> constructor(private val materialSpinner: ComplexDialogSpinnerInput<T>) : ObservableOnSubscribe<Maybe<T>> {
	
	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<Maybe<T>>) {
		materialSpinner.onItemSelectedListener = object : ComplexSpinnerInput.OnItemSelectedListener<T> {
			override fun onItemSelected(parent: ComplexSpinnerInput<T>, item: T?) { subscriber.onNext(Maybe(item)) }
			override fun onNothingSelected(parent: ComplexSpinnerInput<T>) { subscriber.onNext(Maybe(null)) }
		}
		subscriber.setCancellable { materialSpinner.onItemSelectedListener = null }
	}

}