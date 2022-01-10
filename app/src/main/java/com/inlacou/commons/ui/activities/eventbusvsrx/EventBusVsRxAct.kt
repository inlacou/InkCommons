package com.inlacou.commons.ui.activities.eventbusvsrx

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.inlacou.commons.R
import com.inlacou.commons.databinding.ActivityEventbusVsRxBinding
import com.inlacou.commons.eventbusvsrx.Event1
import com.inlacou.commons.eventbusvsrx.Event2
import com.inlacou.commons.eventbusvsrx.Event3
import com.inlacou.commons.eventbusvsrx.Event4
import com.inlacou.commons.ui.activities.BaseAct
import com.inlacou.exinput.rx.clicks
import com.inlacou.inkandroidextensions.toast
import com.inlacou.inkkotlinextensions.fromJson
import com.inlacou.inkkotlinextensions.rx.EventBusChannel
import com.inlacou.inkkotlinextensions.toJson
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

@SuppressLint("SetTextI18n")
class EventBusVsRxAct : BaseAct() {

	private lateinit var model: EventBusVsRxActMdl
	private val controller: EventBusVsRxActCtrl by lazy { baseController as EventBusVsRxActCtrl }

	private var binder: ActivityEventbusVsRxBinding? = null // Example binding var lContent: View? get() = binder?.lContent
	private val eventbusSticky1 get() = binder?.eventbusSticky1
	private val eventbusSticky2 get() = binder?.eventbusSticky2
	private val eventbusSticky3 get() = binder?.eventbusSticky3
	private val eventbusSticky4 get() = binder?.eventbusSticky4
	private val tvEventbusAccess1 get() = binder?.tvEventbusAccess1
	private val tvEventbusAccess2 get() = binder?.tvEventbusAccess2
	private val tvEventbusAccess3 get() = binder?.tvEventbusAccess3
	private val tvEventbusAccess4 get() = binder?.tvEventbusAccess4
	private val tvRxAccess1 get() = binder?.tvRxAccess1
	private val tvRxAccess2 get() = binder?.tvRxAccess2
	private val tvRxAccess3 get() = binder?.tvRxAccess3
	private val tvRxAccess4 get() = binder?.tvRxAccess4
	private val eventbusNonSticky1 get() = binder?.eventbusNonSticky1
	private val eventbusNonSticky2 get() = binder?.eventbusNonSticky2
	private val eventbusNonSticky3 get() = binder?.eventbusNonSticky3
	private val eventbusNonSticky4 get() = binder?.eventbusNonSticky4
	private val tvRxSticky1 get() = binder?.tvRxSticky1
	private val tvRxSticky2 get() = binder?.tvRxSticky2
	private val tvRxSticky3 get() = binder?.tvRxSticky3
	private val tvRxSticky4 get() = binder?.tvRxSticky4
	private val tvRxNonSticky1 get() = binder?.tvRxNonSticky1
	private val tvRxNonSticky2 get() = binder?.tvRxNonSticky2
	private val tvRxNonSticky3 get() = binder?.tvRxNonSticky3
	private val tvRxNonSticky4 get() = binder?.tvRxNonSticky4
	private val btn get() = binder?.btn

	companion object {
		fun navigate(activity: Activity, requestCode: Int? = null, model: EventBusVsRxActMdl = EventBusVsRxActMdl()) {
			if (requestCode != null) activity.startActivityForResult(intent(activity, model), requestCode)
			else activity.startActivity(intent(activity, model))
		}

		fun intent(context: Context, model: EventBusVsRxActMdl = EventBusVsRxActMdl()): Intent {
			return Intent(context, this::class.java.declaringClass).apply {
				putExtra("model", model.toJson())
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binder = ActivityEventbusVsRxBinding.inflate(layoutInflater).also { setContentView(it.root) }

		getIntentData()

		initialize(savedInstanceState)

		configureActionBar()

		populate()

		setListeners()
	}

	private fun getIntentData() {
		model = intent!!.extras!!.getString("model")!!.fromJson() ?: EventBusVsRxActMdl()
	}

	private fun configureActionBar() {
		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar?.title = "EventBus vs Rx"
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
		}
	}

	override fun initialize(savedInstanceState: Bundle?) {
		super.initialize(savedInstanceState)
		baseController = EventBusVsRxActCtrl(view = this, model = model)
	}

	private fun populate() {
		tvEventbusAccess1?.text = "1: -"
		tvEventbusAccess2?.text = "2: -"
		tvEventbusAccess3?.text = "3: -"
		tvEventbusAccess4?.text = "4: -"

		eventbusSticky1?.text = "1: -"
		eventbusSticky2?.text = "2: -"
		eventbusSticky3?.text = "3: -"
		eventbusSticky4?.text = "4: -"

		eventbusNonSticky1?.text = "1: -"
		eventbusNonSticky2?.text = "2: -"
		eventbusNonSticky3?.text = "3: -"
		eventbusNonSticky4?.text = "4: -"

		tvRxAccess1?.text = "1: -"
		tvRxAccess2?.text = "2: -"
		tvRxAccess3?.text = "3: -"
		tvRxAccess4?.text = "4: -"

		tvRxSticky1?.text = "1: -"
		tvRxSticky2?.text = "2: -"
		tvRxSticky3?.text = "3: -"
		tvRxSticky4?.text = "4: -"

		tvRxNonSticky1?.text = "1: -"
		tvRxNonSticky2?.text = "2: -"
		tvRxNonSticky3?.text = "3: -"
		tvRxNonSticky4?.text = "4: -"

		tvEventbusAccess1?.text = "1: ${EventBus.getDefault().getStickyEvent(Event1::class.java)?.s}"
		tvEventbusAccess2?.text = "2: ${EventBus.getDefault().getStickyEvent(Event2::class.java)?.s}"
		tvEventbusAccess3?.text = "3: ${EventBus.getDefault().getStickyEvent(Event3::class.java)?.s}"
		tvEventbusAccess4?.text = "4: ${EventBus.getDefault().getStickyEvent(Event4::class.java)?.s}"

		tvRxAccess1?.text = "1: ${EventBusChannel.getStickyEvent(Event1::class.java, permanent = true)?.s}"
		tvRxAccess2?.text = "2: ${EventBusChannel.getStickyEvent(Event2::class.java, permanent = true)?.s}"
		tvRxAccess3?.text = "3: ${EventBusChannel.getStickyEvent<Event3>(permanent = true)?.s}"
		tvRxAccess4?.text = "4: ${EventBusChannel.getStickyEvent<Event4>(permanent = true)?.s}"
	}

	private fun setListeners() {
		//Register to EventBus
		EventBus.getDefault().register(this)

		//Register to RX EventBusChannel
		disposables.add(EventBusChannel.filteredObs<Event1>(sticky = true).subscribe({ tvRxSticky1?.text = "1: ${it.s}" }, { toast(it.message ?: "Unknown error happened") }))
		disposables.add(EventBusChannel.filteredObs<Event1>(sticky = false).subscribe({ tvRxNonSticky1?.text = "1: ${it.s}" }, { toast(it.message ?: "Unknown error happened") }))
		disposables.add(EventBusChannel.filteredObs<Event2>(sticky = true).subscribe({ tvRxSticky2?.text = "2: ${it.s}" }, { toast(it.message ?: "Unknown error happened") }))
		disposables.add(EventBusChannel.filteredObs<Event2>(sticky = false).subscribe({ tvRxNonSticky2?.text = "2: ${it.s}" }, { toast(it.message ?: "Unknown error happened") }))
		disposables.add(EventBusChannel.filteredObs<Event3>(sticky = true).subscribe({ tvRxSticky3?.text = "3: ${it.s}" }, { toast(it.message ?: "Unknown error happened") }))
		disposables.add(EventBusChannel.filteredObs<Event3>(sticky = false).subscribe({ tvRxNonSticky3?.text = "3: ${it.s}" }, { toast(it.message ?: "Unknown error happened") }))
		disposables.add(EventBusChannel.filteredObs<Event4>(sticky = true).subscribe({ tvRxSticky4?.text = "4: ${it.s}" }, { toast(it.message ?: "Unknown error happened") }))
		disposables.add(EventBusChannel.filteredObs<Event4>(sticky = false).subscribe({ tvRxNonSticky4?.text = "4: ${it.s}" }, { toast(it.message ?: "Unknown error happened") }))

		//UI button listener
		btn?.clicks()?.subscribe({
			EventBusChannel.postSticky(Event1("forced by button click"))
			EventBus.getDefault().postSticky(Event1("forced by button click"))
			EventBusChannel.postSticky(Event2("forced by button click"))
			EventBus.getDefault().postSticky(Event2("forced by button click"))
			EventBusChannel.postSticky(Event3("forced by button click"))
			EventBus.getDefault().postSticky(Event3("forced by button click"))
			EventBusChannel.postSticky(Event4("forced by button click"))
			EventBus.getDefault().postSticky(Event4("forced by button click"))
			Timber.d("forced by button click")
			toast("forced by button click")
		}, { toast(it.message ?: "Unknown error happened") })
	}

	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true) fun onEvent1Sticky(event: Event1?) { eventbusSticky1?.text = "1: ${event?.s}" }
	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true) fun onEvent2Sticky(event: Event2?) { eventbusSticky2?.text = "2: ${event?.s}" }
	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true) fun onEvent3Sticky(event: Event3?) { eventbusSticky3?.text = "3: ${event?.s}" }
	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true) fun onEvent4Sticky(event: Event4?) { eventbusSticky4?.text = "4: ${event?.s}" }

	@Subscribe(threadMode = ThreadMode.MAIN, sticky = false) fun onEvent1NonSticky(event: Event1?) { eventbusNonSticky1?.text = "1: ${event?.s}" }
	@Subscribe(threadMode = ThreadMode.MAIN, sticky = false) fun onEvent2NonSticky(event: Event2?) { eventbusNonSticky2?.text = "2: ${event?.s}" }
	@Subscribe(threadMode = ThreadMode.MAIN, sticky = false) fun onEvent3NonSticky(event: Event3?) { eventbusNonSticky3?.text = "3: ${event?.s}" }
	@Subscribe(threadMode = ThreadMode.MAIN, sticky = false) fun onEvent4NonSticky(event: Event4?) { eventbusNonSticky4?.text = "4: ${event?.s}" }

	override fun onDestroy() {
		//Unsubscribe frm EventBus
		EventBus.getDefault().unregister(this)
		//Unsubscribe from RX EventBusChannel is delegated on super
		super.onDestroy()
	}

}