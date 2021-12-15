package com.inlacou.commons.ui.fragments.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.inlacou.commons.R
import com.inlacou.commons.databinding.FragmentGeneralBinding
import com.inlacou.commons.general.AppCtrl
import com.inlacou.commons.ui.fragments.BaseFrag
import com.inlacou.inkkotlinextensions.fromJson

class GeneralFrag: BaseFrag() {
    
    private var binder: FragmentGeneralBinding? = null
    override val title: String? = AppCtrl.instance.packageName
    
    companion object {
        @JvmOverloads
        fun create(model: GeneralFragMdl = GeneralFragMdl()): GeneralFrag {
            val fragment = GeneralFrag()
            val args = Bundle()
            args.putString("model", Gson().toJson(model))
            fragment.arguments = args
            return fragment
        }
    }
    
    private val controller: GeneralFragCtrl get() = baseController as GeneralFragCtrl
    private lateinit var model: GeneralFragMdl
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_general, container, false)
        binder = FragmentGeneralBinding.bind(rootView)
        
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
        baseController = GeneralFragCtrl(view = this, model = model)
    }
    
    fun populate(rootView: View? = null) {}
    private fun setListeners() {}
}