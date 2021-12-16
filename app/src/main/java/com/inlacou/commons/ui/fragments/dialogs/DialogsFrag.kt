package com.inlacou.commons.ui.fragments.dialogs

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
import com.inlacou.inkandroidextensions.getColorCompat
import com.inlacou.inkandroidextensions.toast
import com.inlacou.inkandroidextensions.view.tint
import com.inlacou.inkbetterandroidviews.adapters.SimpleRvAdapter
import com.inlacou.inkbetterandroidviews.dialogs.input.double.DoubleInputDialog
import com.inlacou.inkbetterandroidviews.dialogs.input.double.DoubleInputDialogMdl
import com.inlacou.inkbetterandroidviews.dialogs.input.integer.IntInputDialog
import com.inlacou.inkbetterandroidviews.dialogs.input.integer.IntInputDialogMdl
import com.inlacou.inkbetterandroidviews.dialogs.input.text.TextInputDialog
import com.inlacou.inkbetterandroidviews.dialogs.input.text.TextInputDialogMdl
import com.inlacou.inkbetterandroidviews.dialogs.list.complex.ComplexListDialog
import com.inlacou.inkbetterandroidviews.dialogs.list.complex.ComplexListDialogMdl
import com.inlacou.inkbetterandroidviews.dialogs.list.simple.SimpleListDialog
import com.inlacou.inkbetterandroidviews.dialogs.list.simple.SimpleListDialogMdl
import com.inlacou.inkbetterandroidviews.dialogs.simple.SimpleDialog
import com.inlacou.inkbetterandroidviews.dialogs.simple.SimpleDialogMdl
import com.inlacou.inkkotlinextensions.calculateCompoundInterestAndLogIt
import com.inlacou.inkkotlinextensions.fromJson
import com.inlacou.inkspannable.InkSpannableBuilder

class DialogsFrag: BaseFrag() {
    
    private var binder: FragmentDialogsBinding? = null
    override val title: String? = AppCtrl.instance.packageName

    val lv: LinearLayout? get() = binder?.lv

    var name = ""
    var intNumber: Int? = null
    var doubleNumber: Double? = null

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
                SimpleDialog(this.context, model = SimpleDialogMdl(
                    title = InkSpannableBuilder().addTextBold("Simple Dialog").build(),
                    content = InkSpannableBuilder().addText("Lorem").addBlank().addTextBold("ipsum").addBlank().addText("dolor sit amet").build()
                )).show()
            }
        })
        lv?.addView(Button(this.context).apply {
            text = "Simple List dialog"
            setOnClickListener {
                SimpleListDialog(this.context, model = SimpleListDialogMdl(
                    title = InkSpannableBuilder().addTextBold("Simple List dialog").build(),
                    items = listOf(Row("Perro"), Row("Gato"), Row("Pez"), Row("Pájaro")),
                    onItemSelected = { requireActivity().toast(it.displayAsRow) }
                )).show()
            }
        })
        lv?.addView(Button(this.context).apply {
            text = "Complex List dialog"
            setOnClickListener {
                ComplexListDialog<LinearLayout, ImageText>(this.context, model = ComplexListDialogMdl(
                    title = InkSpannableBuilder().addTextBold("Complex List dialog").build(),
                    itemLayoutResId = R.layout.recyclerview_item_image_text, /* Set desired layout here */
                    items = listOf(ImageText("Perro", iconTintColorResId = R.color.basic_red),
                        ImageText("Gato", iconTintColorResId = R.color.basic_blue),
                        ImageText("Pez", iconTintColorResId = R.color.basic_green),
                        ImageText("Pájaro", iconTintColorResId = R.color.basic_pink)),
                    onViewPopulate = { dialog, layout, model ->
                        /* Populate provided layout here */
                        layout.findViewById<TextView>(R.id.text).text = model.name
                        layout.findViewById<ImageView>(R.id.icon).apply {
                            setImageResource(model.resId)
                            tint(model.iconTintColorResId)
                        }
                        layout.setOnClickListener {
                            requireActivity().toast(model.name)
                            dialog.dismiss()
                        }
                    }
                )).show()
            }
        })
        lv?.addView(Button(this.context).apply {
            text = "Text input dialog"
            setOnClickListener {
                TextInputDialog(this.context, model = TextInputDialogMdl(
                    title = InkSpannableBuilder().addTextBold("Text input dialog").build(),
                    content = InkSpannableBuilder().addText("Lorem").addBlank().addTextColor("ipsum", context.getColorCompat(R.color.red_text)).addBlank().addText("dolor sit amet").build(),
                    prefix = "prefix",
                    prefixColorResId = R.color.red_text,
                    hint = "Name",
                    input = name,
                    onAccepted = {
                        name = it
                        requireActivity().toast(it)
                    }
                )).show()
            }
        })
        lv?.addView(Button(this.context).apply {
            text = "Integer input dialog"
            setOnClickListener {
                IntInputDialog(this.context, model = IntInputDialogMdl(
                    title = InkSpannableBuilder().addTextBold("Integer input dialog").build(),
                    content = InkSpannableBuilder().addText("Lorem").addBlank().addTextBold("ipsum").addBlank().addText("dolor sit amet").build(),
                    suffix = "units",
                    suffixColorResId = R.color.red_text,
                    hint = "Some value",
                    input = intNumber,
                    onAccepted = {
                        intNumber = it
                        requireActivity().toast(it.toString())
                    }
                )).show()
            }
        })
        lv?.addView(Button(this.context).apply {
            text = "Double input dialog"
            setOnClickListener {
                DoubleInputDialog(this.context, model = DoubleInputDialogMdl(
                    title = InkSpannableBuilder().addTextBold("Double input dialog").build(),
                    content = InkSpannableBuilder().addText("Lorem").addBlank().addTextColor("ipsum", context.getColorCompat(R.color.red_text)).addBlank().addText("dolor sit amet").build(),
                    suffix = "gr",
                    hint = "Some value",
                    input = doubleNumber,
                    onAccepted = {
                        doubleNumber = it
                        requireActivity().toast(it.toString())
                    }
                )).show()
            }
        })
    }

    private fun setListeners() {  }

    class Row(override val displayAsRow: String): SimpleRvAdapter.Row
    class ImageText(val name: String, val resId: Int = R.drawable.space_invader, val iconTintColorResId: Int)
}