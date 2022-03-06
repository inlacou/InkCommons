package com.inlacou.commons.ui.fragments.coroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.inlacou.commons.R
import com.inlacou.commons.databinding.FragmentDialogsBinding
import com.inlacou.commons.general.AppCtrl
import com.inlacou.commons.ui.fragments.BaseFrag
import com.inlacou.inkkotlinextensions.fromJson
import com.inlacou.inkspannable.InkSpannableBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class CoroutinesFrag: BaseFrag() {

    private var binder: FragmentDialogsBinding? = null
    override val title: String? = AppCtrl.instance.packageName

    val lv: LinearLayout? get() = binder?.lv

    var name = ""
    var intNumber: Int? = null
    var doubleNumber: Double? = null

    companion object {
        @JvmOverloads
        fun create(model: CoroutinesFragMdl = CoroutinesFragMdl()): CoroutinesFrag {
            val fragment = CoroutinesFrag()
            val args = Bundle()
            args.putString("model", Gson().toJson(model))
            fragment.arguments = args
            return fragment
        }
    }

    private val controller: CoroutinesFragCtrl get() = baseController as CoroutinesFragCtrl
    private lateinit var model: CoroutinesFragMdl

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
        baseController = CoroutinesFragCtrl(view = this, model = model)
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