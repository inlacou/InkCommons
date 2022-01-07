package com.inlacou.commons.ui.fragments.eventbusvsrx

import com.inlacou.commons.eventbusvsrx.Event1
import com.inlacou.commons.eventbusvsrx.Event2
import com.inlacou.commons.eventbusvsrx.Event3
import com.inlacou.commons.eventbusvsrx.Event4
import com.inlacou.commons.ui.activities.eventbusvsrx.EventBusVsRxAct
import com.inlacou.commons.ui.fragments.BaseFragCtrl
import com.inlacou.inkkotlinextensions.rx.EventBusChannel
import org.greenrobot.eventbus.EventBus

class EventBusVsRxFragCtrl(val view: EventBusVsRxFrag, val model: EventBusVsRxFragMdl) : BaseFragCtrl(view, model) {

	fun onSticky1Changed(s: String) {
		EventBusChannel.postSticky(Event1(s), permanent = true)
		EventBus.getDefault().postSticky(Event1(s))
	}

	fun onSticky2Changed(s: String) {
		EventBusChannel.postSticky(Event2(s), permanent = true)
		EventBus.getDefault().postSticky(Event2(s))
	}

	fun onSticky3Changed(s: String) {
		EventBusChannel.postSticky(Event3(s))
		EventBus.getDefault().postSticky(Event3(s))
	}

	fun onSticky4Changed(s: String) {
		EventBusChannel.postSticky(Event4(s))
		EventBus.getDefault().postSticky(Event4(s))
	}

	fun onNonSticky1Changed(s: String) {
		EventBusChannel.post(Event1(s))
		EventBus.getDefault().post(Event1(s))
	}

	fun onNonSticky2Changed(s: String) {
		EventBusChannel.post(Event2(s))
		EventBus.getDefault().post(Event2(s))
	}

	fun onNonSticky3Changed(s: String) {
		EventBusChannel.post(Event3(s))
		EventBus.getDefault().post(Event3(s))
	}

	fun onNonSticky4Changed(s: String) {
		EventBusChannel.post(Event4(s))
		EventBus.getDefault().post(Event4(s))
	}

	fun onBtnPopulateClick() {
		view.sticky1?.text = "sticky 1"
		view.sticky2?.text = "sticky 2"
		view.sticky3?.text = "sticky 3"
		view.sticky4?.text = "sticky 4"
		view.nonSticky1?.text = "non sticky 1"
		view.nonSticky2?.text = "non sticky 2"
		view.nonSticky3?.text = "non sticky 3"
		view.nonSticky4?.text = "non sticky 4"
	}

	fun onBtnNextClick() {
		EventBusVsRxAct.navigate(view.requireActivity())
	}

}