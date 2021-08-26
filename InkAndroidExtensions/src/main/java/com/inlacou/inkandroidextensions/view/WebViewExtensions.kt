package com.inlacou.inkandroidextensions.view

import android.webkit.WebView

//WebView

var WebView.javascriptEnabled
	get() = settings.javaScriptEnabled
	set(value) {
		settings.javaScriptEnabled=value
	}

///WebView