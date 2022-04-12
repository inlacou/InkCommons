package com.inlacou.commons.ui.fragments.coroutines.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.gson.Gson
import com.inlacou.commons.R
import com.inlacou.commons.databinding.FragmentDialogsBinding
import com.inlacou.commons.general.AppCtrl
import com.inlacou.commons.ui.fragments.BaseFrag
import com.inlacou.inkkotlinextensions.fromJson
import timber.log.Timber

class CoroutinesPlaygroundFrag: BaseFrag() {

    private var binder: FragmentDialogsBinding? = null
    override val title: String? = AppCtrl.instance.packageName

    val lv: LinearLayout? get() = binder?.lv

    var name = ""
    var intNumber: Int? = null
    var doubleNumber: Double? = null

    companion object {
        @JvmOverloads
        fun create(model: CoroutinesPlaygroundFragMdl = CoroutinesPlaygroundFragMdl()): CoroutinesPlaygroundFrag {
            val fragment = CoroutinesPlaygroundFrag()
            val args = Bundle()
            args.putString("model", Gson().toJson(model))
            fragment.arguments = args
            return fragment
        }
    }

    private val controller: CoroutinesPlaygroundFragCtrl get() = baseController as CoroutinesPlaygroundFragCtrl
    private lateinit var model: CoroutinesPlaygroundFragMdl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_coroutines, container, false)
        binder = FragmentDialogsBinding.bind(rootView)

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
        baseController = CoroutinesPlaygroundFragCtrl(view = this, model = model)
    }


    fun populate(rootView: View? = null) {
        controller.populate()
    }

    private fun setListeners() {}

    fun showError(throwable: Throwable) {
        //TODO
        Timber.w("TODO show error on UI ${throwable.message}")
    }
}