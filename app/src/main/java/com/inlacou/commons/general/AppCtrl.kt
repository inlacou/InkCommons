package com.inlacou.commons.general

import androidx.multidex.MultiDexApplication
import com.inlacou.inkspannable.InkSpannableConfig
import timber.log.Timber
import java.lang.ref.WeakReference

class AppCtrl: MultiDexApplication() {

	override fun onCreate() {
		super.onCreate()
		instance = this
		Timber.plant(Timber.DebugTree())
		InkSpannableConfig.context = WeakReference(this)
	}

	companion object {
		lateinit var instance: AppCtrl
	}

}
