package com.inlacou.inkandroidextensions

import org.json.JSONObject

fun JSONObject.removeTag(tag: String): JSONObject {
	var aux = this.toString()
	while(aux.contains(tag)) {
		aux.removeRange(aux.indexOf(tag)-tag.length, 2)
	}
	return JSONObject(aux)
}
