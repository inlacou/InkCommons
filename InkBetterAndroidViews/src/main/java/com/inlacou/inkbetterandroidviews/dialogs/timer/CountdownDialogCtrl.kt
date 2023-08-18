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
	var remainingTime = model.time

	fun start() {
		disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
			.onComputation()
			.toUi()
			.subscribe({
				Timber.d("current: $it")
				remainingTime--
				val s = remainingTime % 60
				val m = remainingTime / 60
				view.setText(if(model.time>60) "$m:$s" else "$s")
			},{
				throw it
			})
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