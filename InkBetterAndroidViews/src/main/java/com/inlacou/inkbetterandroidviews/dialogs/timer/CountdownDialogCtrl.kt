package com.inlacou.inkbetterandroidviews.dialogs.timer

import com.inlacou.inkandroidextensions.onComputation
import com.inlacou.inkandroidextensions.toUi
import com.inlacou.inkbetterandroidviews.dialogs.basic.BasicDialogCtrl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

open class CountdownDialogCtrl(override val view: CountdownDialog, override val model: CountdownDialogMdl): BasicDialogCtrl(view, model) {

	protected var timerDisposable: Disposable? = null
	protected var remainingTime = model.time
		set(value) {
			field = value
			updateShownTime(value)
			onRemainingTime(value)
		}

	/**
	 * Method to start the timer. The timer is controlled by [timerDisposable]. The time is in [remainingTime].
	 */
	open fun start() {
		timerDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
			.onComputation()
			.toUi()
			.subscribe({
				remainingTime--
			},{
				throw it
			})
	}

	/**
	 * Method to stop or resume the timer.
	 */
	open fun onStopResume() {
		if(timerDisposable != null) timerDisposable?.dispose()
		else start()
	}

	/**
	 * Method changed with each change on the value [remainingTime] to update UI.
	 * Other reactions are handled in [onRemainingTime].
	 */
	open fun updateShownTime(remainingTime: Int) {
		view.setTimerText(if(model.time > 60) {
			"${remainingTime / 60}:${remainingTime % 60}"
		} else {
			"$remainingTime"
		})
	}

	/**
	 * Method changed with each change on the value [remainingTime] to react to it.
	 * Updating the UI is not here, it's on [updateShownTime].
	 */
	open fun onRemainingTime(remainingTime: Int) {
		if(remainingTime==0) model.onTimerFinished?.invoke(view)
	}

	override fun onDestroy() {
		timerDisposable?.dispose()
		super.onDestroy()
	}

	override fun onCancelClick() {
		timerDisposable?.dispose()
		super.onCancelClick()
	}

}