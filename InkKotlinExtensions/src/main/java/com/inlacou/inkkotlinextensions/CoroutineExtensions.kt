package com.inlacou.inkkotlinextensions

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
	require(periodMillis > 0) { "periodMillis should be positive" }
	return flow {
		var lastTime = 0L
		collect { value ->
			val currentTime = System.currentTimeMillis()
			if (currentTime-lastTime >= periodMillis) {
				lastTime = currentTime
				emit(value)
			}
		}
	}
}

