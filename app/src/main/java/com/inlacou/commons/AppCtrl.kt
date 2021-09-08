package com.inlacou.commons

import androidx.multidex.MultiDexApplication
import timber.log.Timber

class AppCtrl : MultiDexApplication() {

	override fun onCreate() {
		super.onCreate()
		Timber.plant(Timber.DebugTree())
	}

}
