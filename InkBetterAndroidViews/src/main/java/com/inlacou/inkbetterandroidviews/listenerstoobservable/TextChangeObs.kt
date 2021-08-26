package com.inlacou.inkbetterandroidviews.listenerstoobservable

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class TextChangeObs constructor(private val view: TextView) : ObservableOnSubscribe<String> {

	private lateinit var listener: TextWatcher

	@Throws(Exception::class)
	override fun subscribe(subscriber: ObservableEmitter<String>) {
		listener = object: TextWatcher{
			override fun afterTextChanged(s: Editable?) {
				subscriber.onNext(s?.toString() ?: "")
			}

			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			}
		}

		view.addTextChangedListener(listener)

		subscriber.setCancellable { view.removeTextChangedListener(listener) }
	}
}