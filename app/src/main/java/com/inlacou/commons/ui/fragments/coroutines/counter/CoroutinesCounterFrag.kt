package com.inlacou.commons.ui.fragments.coroutines.counter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.inlacou.commons.R
import com.inlacou.commons.databinding.FragmentCounterBinding
import com.inlacou.inkandroidextensions.clicks
import com.inlacou.inkbetterandroidviews.extensions.longClickSpeedingFiringIntervalsFlow
import com.inlacou.inkkotlinextensions.throttleFirst
import com.inlacou.inkspannable.InkSpannableBuilder
import kotlinx.coroutines.flow.collect

class CoroutinesCounterFrag: Fragment() {
    
    private var binder: FragmentCounterBinding? = null

    companion object {
        @JvmOverloads
        fun create(model: Int = 0): CoroutinesCounterFrag {
            val fragment = CoroutinesCounterFrag()
            val args = Bundle()
            args.putInt("model", model)
            fragment.arguments = args
            return fragment
        }
    }
    
    private val model: CoroutinesCounterVwMdl = CoroutinesCounterVwMdl()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_counter, container, false)
        binder = FragmentCounterBinding.bind(rootView)
        
        getArgs()

        initialize(rootView, savedInstanceState)
        
        populate(rootView)
        
        setListeners()
        
        return rootView
    }

    private fun getArgs() {
        arguments?.getInt("model")?.let { model.set(it) }
    }

    protected fun initialize(rootView: View, savedInstanceState: Bundle?) {}
    
    fun populate(rootView: View? = null) {}
    
    private fun setListeners() {
        binder?.btn1?.text = "${binder?.btn1?.text?.toString()} (filter rapid clicks 1000ms)"
        binder?.btn2?.text = "${binder?.btn2?.text?.toString()} (keep clicked for shorter intervals)"
        lifecycleScope.launchWhenResumed {
            model.sourceFlow.collect { binder?.tvLinear?.text = InkSpannableBuilder().addTextBold("Linear:").addBlank().addText(it.toString()).build() /* Execution stops here */ }
        }
        lifecycleScope.launchWhenResumed {
            model.transformedFlow.collect { binder?.tvGeometric?.text = InkSpannableBuilder().addTextBold("Geometric:").addBlank().addText(it.toString()).build() }
        }
        lifecycleScope.launchWhenResumed {
            binder?.btn1?.clicks()?.throttleFirst(1000)?.collect { model.increment() }
        }
        lifecycleScope.launchWhenResumed {
            binder?.btn2?.longClickSpeedingFiringIntervalsFlow()?.collect { model.increment() }
        }

        /*binder?.dummy?.longClickSpeedingFiringIntervals()?.toUi()?.subscribe({
            model.increment()
        },{
            binder?.tvLinear?.text = it.message
            binder?.tvGeometric?.text = it.message
        })*/
    }

}