package com.inlacou.commons.ui.fragments.betterspinner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.inlacou.commons.R
import com.inlacou.commons.business.Tag
import com.inlacou.commons.databinding.FragmentBetterSpinnerBinding
import com.inlacou.commons.general.AppCtrl
import com.inlacou.commons.ui.fragments.BaseFrag
import com.inlacou.inkandroidextensions.toast
import com.inlacou.inkbetterandroidviews.extensions.itemClicks
import com.inlacou.inkbetterandroidviews.spinners.BetterSpinner
import com.inlacou.inkkotlinextensions.fromJson
import timber.log.Timber

class BetterSpinnerFrag: BaseFrag() {

    private var binder: FragmentBetterSpinnerBinding? = null
    override val title: String = AppCtrl.instance.getString(R.string.TextView_Bitmap)

    val items = listOf("Platano", "Tomate",
        "Manzana Golden", "Manzana Granny Smith", "Manzana roja",
        "Pera", "Kaki", "DragonFruit", "Guayaba").sorted()

    val complexItems = listOf(ComplexItem("Platano", "Fruta"), ComplexItem("Tomate", "Fruta"),
        ComplexItem("Espinaca", "Verdura"), ComplexItem("Lechuga", "Verdura"), ComplexItem("Col", "Verdura"),
        ComplexItem("Pato", "Carne"), ComplexItem("Ternera", "Carne"), ComplexItem("Pollo", "Carne"),
        ComplexItem("Manzana Golden", "Fruta"), ComplexItem("Manzana Granny Smith", "Fruta"), ComplexItem("Manzana roja", "Fruta"),
        ComplexItem("Pera", "Fruta"), ComplexItem("Kaki", "Fruta"), ComplexItem("DragonFruit", "Fruta"), ComplexItem("Guayaba", "Fruta")).sortedBy { it.display }

    val tags = listOf(
        Tag(name = "Red", colorHexadecimal = "#FF0000", iconResourceName = "space_invader", typeName = "Color"),
        Tag(name = "Green", colorHexadecimal = "#00FF00", iconResourceName = "space_invader", typeName = "Color"),
        Tag(name = "Blue", colorHexadecimal = "#0000FF", iconResourceName = "space_invader", typeName = "Color"),
        Tag(name = "Banana", colorHexadecimal = "#FAFAD2", iconResourceName = "space_invader", typeName = "Fruit"),
        Tag(name = "Apple", colorHexadecimal = "#DD1811", iconResourceName = "space_invader", typeName = "Fruit"),
        Tag(name = "Kiwi", colorHexadecimal = "#9B673C", iconResourceName = "space_invader", typeName = "Fruit"),
    )

    data class ComplexItem(override val display: String, val category: String): BetterSpinner.ComplexItem {
        override fun filter(s: String): Boolean = display.contains(s, ignoreCase = true) || category.contains(s, ignoreCase = true)
    }

    val root get() = binder?.root
    val betterSpinner get() = binder?.betterSpinner
    val betterSpinnerFilterable get() = binder?.betterSpinnerFilterable
    val betterSpinnerFilterableComplex get() = binder?.betterSpinnerFilterableComplex
    val tagSpinner get() = binder?.tagSpinner
    val tagSpinnerFilterable get() = binder?.tagSpinnerFilterable

    companion object {
        @JvmOverloads
        fun create(model: BetterSpinnerFragMdl = BetterSpinnerFragMdl()): BetterSpinnerFrag {
            val fragment = BetterSpinnerFrag()
            val args = Bundle()
            args.putString("model", Gson().toJson(model))
            fragment.arguments = args
            return fragment
        }
    }

    private val controller: BetterSpinnerFragCtrl get() = baseController as BetterSpinnerFragCtrl
    private lateinit var model: BetterSpinnerFragMdl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_better_spinner, container, false)
        binder = FragmentBetterSpinnerBinding.bind(rootView)

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
        baseController = BetterSpinnerFragCtrl(view = this, model = model)
    }

    fun populate(rootView: View? = null) {
        betterSpinner?.allowFilter = false
        betterSpinner?.clearOnClick = false
        betterSpinner?.setSimpleAdapter(items)
        betterSpinnerFilterable?.allowFilter = true
        betterSpinnerFilterable?.clearOnClick = true
        betterSpinnerFilterable?.setSimpleAdapter(items)
        betterSpinnerFilterableComplex?.allowFilter = true
        betterSpinnerFilterableComplex?.clearOnClick = true
        betterSpinnerFilterableComplex?.setComplexAdapter(complexItems)

        tagSpinner?.setTagAdapter(tags)
        tagSpinnerFilterable?.allowFilter = true
        tagSpinnerFilterable?.clearOnClick = true
        tagSpinnerFilterable?.setTagAdapter(tags)
    }

    private fun setListeners() {
        betterSpinner?.itemClicks()?.subscribe({ activity?.toast("Selected #${it.first}: ${it.second}") }, { Timber.e(it); activity?.toast(it.message ?: "Unknown error happened") })
        betterSpinnerFilterable?.itemClicks()?.subscribe({ activity?.toast("Selected #${it.first}: ${it.second}") }, { Timber.e(it); activity?.toast(it.message ?: "Unknown error happened") })
        betterSpinnerFilterableComplex?.itemClicks()?.subscribe({ activity?.toast("Selected #${it.first}: ${it.second}") }, { Timber.e(it); activity?.toast(it.message ?: "Unknown error happened") })
        tagSpinner?.itemClicks()?.subscribe({ activity?.toast("Selected #${it.first}: ${it.second}") }, { Timber.e(it); activity?.toast(it.message ?: "Unknown error happened") })
        tagSpinnerFilterable?.itemClicks()?.subscribe({ activity?.toast("Selected #${it.first}: ${it.second}") }, { Timber.e(it); activity?.toast(it.message ?: "Unknown error happened") })
    }
}