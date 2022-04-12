package com.inlacou.inkbetterandroidviews.extensions

import android.view.MotionEvent
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.TextView
import com.inlacou.inkbetterandroidviews.listenerstoflow.OnLongTouchIncrementFiringSpeedFlow
import com.inlacou.inkbetterandroidviews.listenerstoobservable.*
import com.inlacou.inkbetterandroidviews.listenerstoobservable.AutoCompleteTextViewObs
import io.reactivex.rxjava3.core.Observable

/* UI */
fun View.clicks(): Observable<View> = Observable.create(OnClickObs(this))
fun View.longClicks(cosumeEvent: Boolean = true): Observable<View> = Observable.create(OnLongClickObs(this, consumeEvent = cosumeEvent))
fun AutoCompleteTextView.itemClicks(): Observable<Pair<Int, Any>> = Observable.create(AutoCompleteTextViewObs(this))
fun View.touchs(): Observable<Triple<MotionEvent, Float, Float>> = Observable.create(OnTouchObs(this))
fun TextView.textChanges(): Observable<String> = Observable.create(TextChangeObs(this))
fun CheckBox.checkedChanges(): Observable<Boolean> = Observable.create(CheckBoxObs(this))
fun View.layoutChanges(): Observable<Triple<View, LayoutChangeObs.Dimensions, LayoutChangeObs.Dimensions>> = Observable.create(LayoutChangeObs(this))

val slowFiringInterval = listOf(Pair(1500, 500))
fun View.longClickSpeedingFiringIntervalsObs(breakpointsAndSpeeds: List<Pair<Int, Int>>? = null): Observable<Long> = Observable.create(OnLongTouchIncrementFiringSpeedObs(this, breakpointsAndSpeeds))

/*fun InkSwitch.changes(): Observable<Pair<Int, Boolean>> {
	return Observable.create(InkSwitchObs(this))
}*/
/* /UI */