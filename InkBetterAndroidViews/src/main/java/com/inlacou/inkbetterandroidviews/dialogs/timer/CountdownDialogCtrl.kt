package com.inlacou.inkbetterandroidviews.dialogs.timer

import com.inlacou.inkandroidextensions.onComputation
import com.inlacou.inkandroidextensions.toUi
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogCtrl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CountdownDialogCtrl(override val view: CountdownDialog, override val model: CountdownDialogMdl): BasicDialogCtrl(view, model) {

	private var disposable: Disposable? = null
	private var remainingTime = model.time
		set(value) {
			field = value
			updateShownTime(value)
			onRemainingTime(value)
		}

	fun start() {
		disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
			.onComputation()
			.toUi()
			.subscribe({
				remainingTime--
			},{
				throw it
			})
	}

	open fun updateShownTime(remainingTime: Int) {
		view.setText(if(model.time > 60) {
			"${remainingTime / 60}:${remainingTime % 60}"
		} else {
			"$remainingTime"
		})
	}

	open fun onRemainingTime(remainingTime: Int) {
		if(remainingTime==0) model.onTimerFinished?.invoke(view)
	}

	fun onStopResume() {
		if(disposable != null) disposable?.dispose()
		else start()
	}

	override fun onDestroy() {
		disposable?.dispose()
		super.onDestroy()
	}

}