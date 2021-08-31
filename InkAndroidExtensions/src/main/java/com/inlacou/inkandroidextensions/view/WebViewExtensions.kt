package com.inlacou.inkandroidextensions.view

import android.webkit.WebView

var WebView.javascriptEnabled
	get() = settings.javaScriptEnabled
	set(value) {
		settings.javaScriptEnabled=value
	}