package com.inlacou.commons.general

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.multidex.MultiDexApplication
import com.inlacou.inkkotlinextensions.fromJson
import com.inlacou.inkkotlinextensions.rx.EventBusChannel
import com.inlacou.inkpersistor.GenericSharedPrefMngr
import com.inlacou.inkspannable.InkSpannableConfig
import timber.log.Timber
import java.lang.ref.WeakReference

class AppCtrl: MultiDexApplication() {

	override fun onCreate() {
		super.onCreate()
		instance = this
		Timber.plant(Timber.DebugTree())
		InkSpannableConfig.context = WeakReference(this)

		EventBusChannel.removePermanent = { GenericSharedPrefMngr.erase(this, key = "EventBusChannel.${it.name}") }
		EventBusChannel.loadPermanent = { getItem(this, key = "EventBusChannel.${it.name}", clazz = it).also {
			Timber.d("DEBUGINLACOU | aux: $it")
		} }
		EventBusChannel.savePermanent = { setItem(this, key = "EventBusChannel.${it.javaClass.name}", value = it, clazz = it.javaClass) }
	}

	companion object {
		lateinit var instance: AppCtrl
	}

	inline fun <reified T> getItem(context: Context, key: String, clazz: Class<out T>, sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)): T? {
		Timber.d("saved1 $key... ${sharedPreferences.getString(key, "{}")}")
		Timber.d("DEBUGINLACOU getItem1 | clazz: ${clazz.name} | T: ${T::class.java.name}")
		return sharedPreferences.getString(key, "{}")?.fromJson(clazz)
	}

	inline fun <reified T> setItem(context: Context, key: String, value: T, clazz: Class<out T>, sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context))
	  = GenericSharedPrefMngr.setItem(context, key, value, sharedPreferences)

}
