package com.inlacou.commons.ui.fragments.textviewbitmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.gson.Gson
import com.inlacou.commons.R
import com.inlacou.commons.databinding.FragmentTextviewBitmapBinding
import com.inlacou.commons.general.AppCtrl
import com.inlacou.commons.ui.fragments.BaseFrag
import com.inlacou.inkandroidextensions.getColorCompat
import com.inlacou.inkandroidextensions.view.setWidthHeight
import com.inlacou.inkandroidextensions.windowSize
import com.inlacou.inkbetterandroidviews.textviews.TextViewBitmap
import com.inlacou.inkkotlinextensions.fromJson

class TextViewBitmapFrag: BaseFrag() {

    private var binder: FragmentTextviewBitmapBinding? = null
    override val title: String = AppCtrl.instance.getString(R.string.TextView_Bitmap)

    val root get() = binder?.root

    companion object {
        @JvmOverloads
        fun create(model: TextViewBitmapFragMdl = TextViewBitmapFragMdl()): TextViewBitmapFrag {
            val fragment = TextViewBitmapFrag()
            val args = Bundle()
            args.putString("model", Gson().toJson(model))
            fragment.arguments = args
            return fragment
        }
    }

    private val controller: TextViewBitmapFragCtrl get() = baseController as TextViewBitmapFragCtrl
    private lateinit var model: TextViewBitmapFragMdl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_textview_bitmap, container, false)
        binder = FragmentTextviewBitmapBinding.bind(rootView)

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
        baseController = TextViewBitmapFragCtrl(view = this, model = model)
    }

    fun populate(rootView: View? = null) {
        val SPACING = 20
        root?.apply {
            addView(ImageView(context).apply {
                setWidthHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setImageBitmap(
	                TextViewBitmap.Builder(requireContext())
                    .setText("Lorem ipsum dolor sit amet.")
                    .setFontSize(10f)
                    .setIsFontSizeInSp(true)
                    .setMaxLines(2)
                    .setBold(5, 8)
                    .setMaxLines(1)
                    .setBackgroundColor(requireContext().getColorCompat(R.color.basic_red))
                    .setMaxWidth(maxWidth = 500)
                    .build()) })
            addView(View(context).apply {
                setWidthHeight(width = ViewGroup.LayoutParams.WRAP_CONTENT, height = SPACING)
            })
            addView(ImageView(context).apply {
                setWidthHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setImageBitmap(
	                TextViewBitmap.Builder(requireContext())
                    .setText("Lorem ipsum dolor sit amet.")
                    .setMaxLines(2)
                    .setFontSize(10f)
                    .setIsFontSizeInSp(true)
                    .setBold(6, 11)
                    .setBackgroundColor(requireContext().getColorCompat(R.color.basic_red))
                    .setMaxWidth(maxWidth = windowSize().x)
                    .build()) })
            addView(View(context).apply {
                setWidthHeight(width = ViewGroup.LayoutParams.WRAP_CONTENT, height = SPACING)
            })
            addView(ImageView(context).apply {
                setWidthHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setImageBitmap(
	                TextViewBitmap.Builder(requireContext())
                    .setText("Lorem ipsum dolor sit amet.")
                    .setMaxLines(2)
                    .setFontSize(10f)
                    .setIsFontSizeInSp(true)
                    .setBold(6, 11)
                    .setBackgroundColor(requireContext().getColorCompat(R.color.basic_red))
                    .setMaxWidth(maxWidth = windowSize().x+100)
                    .build()) })
            addView(View(context).apply {
                setWidthHeight(width = ViewGroup.LayoutParams.WRAP_CONTENT, height = SPACING)
            })
            addView(ImageView(context).apply {
                setWidthHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setImageBitmap(
	                TextViewBitmap.Builder(requireContext())
                    .setText("Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet.")
                    .setMaxLines(2)
                    .setFontSize(10f)
                    .setIsFontSizeInSp(true)
                    .setBold(6, 11)
                    .setBackgroundColor(requireContext().getColorCompat(R.color.basic_red))
                    .setMaxWidth(maxWidth = windowSize().x/2)
                    .build()) })
            addView(View(context).apply {
                setWidthHeight(width = ViewGroup.LayoutParams.MATCH_PARENT, height = SPACING)
            })
            addView(ImageView(context).apply {
                setWidthHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setImageBitmap(
	                TextViewBitmap.Builder(requireContext())
                    .setText("Quedan 18 días para cerrar el mes.")
                    .setMaxLines(2)
                    .setFontSize(15f)
                    .setIsFontSizeInSp(true)
                    .setBold(7, 7+5+2)
                    .setBackgroundColor(requireContext().getColorCompat(R.color.basic_red))
                    .setMaxWidth(114)
                    .build()) })
            addView(View(context).apply {
                setWidthHeight(width = ViewGroup.LayoutParams.MATCH_PARENT, height = SPACING)
            })
            addView(ImageView(context).apply {
                setWidthHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setImageBitmap(
	                TextViewBitmap.Builder(requireContext())
                    .setText("Quedan 18 días para cerrar el mes")
                    .setMaxLines(2)
                    .setFontSize(15f)
                    .setIsFontSizeInSp(true)
                    .setBold(7, 7+5+2)
                    .setBackgroundColor(requireContext().getColorCompat(R.color.basic_red))
                    .setMaxWidth(554)
                    .build()) })
            addView(View(context).apply {
                setWidthHeight(width = ViewGroup.LayoutParams.MATCH_PARENT, height = SPACING)
            })
        }
    }
    private fun setListeners() {}
}