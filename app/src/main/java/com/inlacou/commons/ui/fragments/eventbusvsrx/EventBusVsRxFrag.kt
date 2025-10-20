package com.inlacou.commons.ui.fragments.eventbusvsrx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.inlacou.commons.R
import com.inlacou.commons.databinding.FragmentEventbusVsRxBinding
import com.inlacou.commons.eventbusvsrx.Event1
import com.inlacou.commons.eventbusvsrx.Event2
import com.inlacou.commons.eventbusvsrx.Event3
import com.inlacou.commons.eventbusvsrx.Event4
import com.inlacou.commons.general.AppCtrl
import com.inlacou.commons.ui.fragments.BaseFrag
import com.inlacou.inkandroidextensions.toast
import com.inlacou.inkbetterandroidviews.extensions.clicks
import com.inlacou.inkbetterandroidviews.extensions.textChanges
import com.inlacou.inkkotlinextensions.fromJson
import com.inlacou.inkkotlinextensions.rx.EventBusChannel

class EventBusVsRxFrag: BaseFrag() {
    
    private var binder: FragmentEventbusVsRxBinding? = null
    val sticky1 get() = binder?.sticky1
    val sticky2 get() = binder?.sticky2
    val sticky3 get() = binder?.sticky3
    val sticky4 get() = binder?.sticky4
    val nonSticky1 get() = binder?.nonSticky1
    val nonSticky2 get() = binder?.nonSticky2
    val nonSticky3 get() = binder?.nonSticky3
    val nonSticky4 get() = binder?.nonSticky4
    val btnNext get() = binder?.btnNext
    val btnPopulate get() = binder?.btnPopulate
    override val title: String? = AppCtrl.instance.packageName
    
    companion object {

        const val permanent1 = true
        const val permanent2 = true
        const val permanent3 = false
        const val permanent4 = false

        @JvmOverloads
        fun create(model: EventBusVsRxFragMdl = EventBusVsRxFragMdl()): EventBusVsRxFrag {
            val fragment = EventBusVsRxFrag()
            val args = Bundle()
            args.putString("model", Gson().toJson(model))
            fragment.arguments = args
            return fragment
        }
    }
    
    private val controller: EventBusVsRxFragCtrl get() = baseController as EventBusVsRxFragCtrl
    private lateinit var model: EventBusVsRxFragMdl
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_eventbus_vs_rx, container, false)
        binder = FragmentEventbusVsRxBinding.bind(rootView)
        
        getArgs()
        
        initialize(rootView, savedInstanceState)
        
        populate(rootView)
        
        setListeners()
        
        return rootView
    }
    
    private fun getArgs() {
        arguments?.getString("model")?.let { model = it.fromJson()!! }
    }
    
    protected fun initialize(rootView: View, savedInstanceState: Bundle?) {
        super.initialize(rootView)
        baseController = EventBusVsRxFragCtrl(view = this, model = model)
    }
    
    fun populate(rootView: View? = null) {
        sticky1?.text = EventBusChannel.getStickyEvent(Event1::class.java, permanent = true)?.s ?: ""
        sticky2?.text = EventBusChannel.getStickyEvent(Event2::class.java, permanent = true)?.s ?: ""
        sticky3?.text = EventBusChannel.getStickyEvent<Event3>(permanent = true)?.s ?: ""
        sticky4?.text = EventBusChannel.getStickyEvent<Event4>(permanent = true)?.s ?: ""
    }
    
    private fun setListeners() {
        btnNext?.clicks()?.subscribe({ controller.onBtnNextClick() }, { activity?.toast(it.message ?: "Unknown error happened") })
        btnPopulate?.clicks()?.subscribe({ controller.onBtnPopulateClick() }, { activity?.toast(it.message ?: "Unknown error happened") })
        sticky1?.textChanges()?.subscribe({ controller.onSticky1Changed(it) }, { activity?.toast(it.message ?: "Unknown error happened") })
        sticky2?.textChanges()?.subscribe({ controller.onSticky2Changed(it) }, { activity?.toast(it.message ?: "Unknown error happened") })
        sticky3?.textChanges()?.subscribe({ controller.onSticky3Changed(it) }, { activity?.toast(it.message ?: "Unknown error happened") })
        sticky4?.textChanges()?.subscribe({ controller.onSticky4Changed(it) }, { activity?.toast(it.message ?: "Unknown error happened") })
        nonSticky1?.textChanges()?.subscribe({ controller.onNonSticky1Changed(it) }, { activity?.toast(it.message ?: "Unknown error happened") })
        nonSticky2?.textChanges()?.subscribe({ controller.onNonSticky2Changed(it) }, { activity?.toast(it.message ?: "Unknown error happened") })
        nonSticky3?.textChanges()?.subscribe({ controller.onNonSticky3Changed(it) }, { activity?.toast(it.message ?: "Unknown error happened") })
        nonSticky4?.textChanges()?.subscribe({ controller.onNonSticky4Changed(it) }, { activity?.toast(it.message ?: "Unknown error happened") })
    }
    
}