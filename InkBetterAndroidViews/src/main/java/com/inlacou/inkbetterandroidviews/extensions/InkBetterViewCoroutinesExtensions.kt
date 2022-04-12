package com.inlacou.inkbetterandroidviews.extensions

import android.view.View
import com.inlacou.inkbetterandroidviews.listenerstoflow.OnLongTouchIncrementFiringSpeedFlow
import kotlinx.coroutines.flow.Flow

fun View.longClickSpeedingFiringIntervalsFlow(breakpointsAndSpeeds: List<Pair<Int, Int>>? = null): Flow<Long> = OnLongTouchIncrementFiringSpeedFlow(this, breakpointsAndSpeeds).create()
