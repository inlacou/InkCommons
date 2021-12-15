package com.inlacou.commons.ui.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SimpleAdapter
import com.google.gson.Gson
import com.inlacou.commons.R
import com.inlacou.commons.databinding.FragmentDialogsBinding
import com.inlacou.commons.general.AppCtrl
import com.inlacou.commons.ui.fragments.BaseFrag
import com.inlacou.inkandroidextensions.toast
import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.list.simple.SimpleListDialogView
import com.inlacou.inkbetterandroidviews.dialogs.list.simple.SimpleListDialogViewMdl
import com.inlacou.inkbetterandroidviews.dialogs.simple.SimpleDialogView
import com.inlacou.inkbetterandroidviews.dialogs.simple.SimpleDialogViewMdl
import com.inlacou.inkkotlinextensions.fromJson
import com.inlacou.inkspannable.InkSpannableBuilder

class DialogsFrag: BaseFrag() {
    
    private var binder: FragmentDialogsBinding? = null
    override val title: String? = AppCtrl.instance.packageName

    val lv: LinearLayout? get() = binder?.lv
    
    companion object {
        @JvmOverloads
        fun create(model: DialogsFragMdl = DialogsFragMdl()): DialogsFrag {
            val fragment = DialogsFrag()
            val args = Bundle()
            args.putString("model", Gson().toJson(model))
            fragment.arguments = args
            return fragment
        }
    }
    
    private val controller: DialogsFragCtrl get() = baseController as DialogsFragCtrl
    private lateinit var model: DialogsFragMdl
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_dialogs, container, false)
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
        baseController = DialogsFragCtrl(view = this, model = model)
    }
    
    fun populate(rootView: View? = null) {
        lv?.addView(Button(this.context).apply {
            text = "Simple dialog"
            setOnClickListener {
                SimpleDialogView(this.context, model = SimpleDialogViewMdl(
                    title = InkSpannableBuilder().addTextBold("Simple Dialog").build(),
                    content = InkSpannableBuilder().addText("Lorem").addBlank().addTextBold("ipsum").addBlank().addText("dolor sit amet").build()
                )).show()
            }
        })
        lv?.addView(Button(this.context).apply {
            text = "List dialog"
            setOnClickListener {
                SimpleListDialogView(this.context, model = SimpleListDialogViewMdl(
                    title = InkSpannableBuilder().addTextBold("List Dialog").build(),
                    items = listOf(Row("Perro"), Row("Gato"), Row("Pez"), Row("Pájaro")), onItemSelected = { requireActivity().toast(it.displayAsRow) }
                )).show()
            }
        })
    }

    private fun setListeners() {  }

    class Row(override val displayAsRow: String): SimpleRvAdapter.Row
}