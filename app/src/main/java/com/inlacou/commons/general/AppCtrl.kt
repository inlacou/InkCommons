package com.inlacou.commons.general

import androidx.multidex.MultiDexApplication
import com.inlacou.inker.Inker
import com.inlacou.inkkotlinextensions.rx.EventBusChannel
import com.inlacou.inkpersistor.GenericSharedPrefMngr
import com.inlacou.inkspannable.InkSpannableConfig
import java.lang.ref.WeakReference

class AppCtrl: MultiDexApplication() {

	override fun onCreate() {
		super.onCreate()
		instance = this
		Inker.mix(InkerDebugColor())
		InkSpannableConfig.context = WeakReference(this)

		EventBusChannel.removePermanent = { GenericSharedPrefMngr.erase(this, key = "EventBusChannel.${it.name}") }
		EventBusChannel.loadPermanent = { GenericSharedPrefMngr.getItem(this, key = "EventBusChannel.${it.name}", clazz = it) }
		EventBusChannel.savePermanent = { GenericSharedPrefMngr.setItem(this, key = "EventBusChannel.${it.javaClass.name}", value = it, clazz = it.javaClass) }
	}

	companion object {
		lateinit var instance: AppCtrl
	}

}
