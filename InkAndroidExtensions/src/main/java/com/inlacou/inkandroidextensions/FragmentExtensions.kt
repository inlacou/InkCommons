package com.inlacou.inkandroidextensions

import android.graphics.Point
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun Fragment.windowSize(): Point {
	val display = activity?.windowManager?.defaultDisplay
	val size = Point()
	display?.getSize(size)
	return size
}

fun FragmentTransaction.removeSafe(frag: Fragment?) {
	if(frag!=null) this.remove(frag)
}

fun FrameLayout.loadFragment(fragmentManager: FragmentManager?, fragment: Fragment, addToBackStack: Boolean = true) {
	fragmentManager?.beginTransaction()?.apply {
		replace(id, fragment)
		if(addToBackStack) addToBackStack(null)
	}?.commit()
}

private fun FrameLayout.currentFragment(supportFragmentManager: FragmentManager): Fragment? {
	return supportFragmentManager.findFragmentById(this.id)
}

/**
 * This method avoids the fragment loading into UI (not creation) if already present on frameLayout
 */
fun FrameLayout.loadFragmentIfNotPresent(fragmentManager: FragmentManager?, fragment: Fragment) {
	if(fragmentManager==null || currentFragment(fragmentManager)?.javaClass==fragment.javaClass) return
	else loadFragment(fragmentManager, fragment)
}

/**
 * This method avoids the fragment creation (not only loading into UI) if already present on frameLayout
 */
fun <T: Fragment> FrameLayout.loadFragmentIfNotPresent(fragmentManager: FragmentManager?, fragClass: Class<T>, initializationFunction: () -> T) {
	if(fragmentManager==null || currentFragment(fragmentManager)?.javaClass==fragClass) return
	else loadFragment(fragmentManager, initializationFunction.invoke())
}

fun FrameLayout.clear(fragmentManager: FragmentManager?) {
	fragmentManager?.beginTransaction()?.apply {
		fragmentManager.findFragmentById(id)?.let { remove(it) }
	}?.commit()
}

fun FrameLayout.getFragment(fragmentManager: FragmentManager?): Fragment? {
	fragmentManager?.findFragmentById(id)?.let { return it }
	return null
}