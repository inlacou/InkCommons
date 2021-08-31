package com.inlacou.inkandroidextensions.utils

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import com.inlacou.inkkotlinextensions.betweenRange

fun UiDevice.makeClick(text: String): Boolean {
	return findObject(UiSelector().text(text)).click()
}
fun UiDevice.makeClick(texts: Array<String>): Boolean = makeClick(texts.toList())
fun UiDevice.makeClick(texts: List<String>): Boolean {
	var clicked = false
	texts.forEach { text ->
		try{
			if(findObject(UiSelector().text(text)).click()) clicked = true
		} catch (uonfoe: UiObjectNotFoundException) {}
	}
	if(!clicked) throw UiObjectNotFoundException("No Ui object found with text any of $texts")
	return clicked
}

fun UiDevice.clickCheckable(text: String): Boolean {
	return findObject(UiSelector().text(text)).let {
		if(it?.isCheckable==true) it.click()
		else false
	}
}

fun UiDevice.makeClickToRightOf(text: String): Boolean {
	return makeClickToRightOf(listOf(text))
}

fun UiDevice.makeClickToRightOf(texts: List<String>): Boolean {
	var clicked = false
	var message = ""
	
	texts.forEach { text ->
		message += "\n ---------------------"
		val tv = findObject(UiSelector().text(text))
		val checkables = findObjects(By.checkable(true))
		
		checkables.forEach {
			message += "\n ${it.visibleBounds.centerY()} vs ${tv.visibleBounds.centerY()}"
			if(it.visibleBounds.centerY().betweenRange(tv.visibleBounds.centerY(), 20)) it.click(); clicked = true
		}
	}
	
	if(!clicked) throw UiObjectNotFoundException("No Ui checkable object found near object with text any of $texts $message")
	return clicked
}
