package com.inlacou.commons.ui.fragments.gradienttint

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.github.koston.preference.view.ColorPicker
import com.github.koston.preference.view.SaturationValueBar
import com.inlacou.commons.R
import com.inlacou.commons.databinding.FragmentGradientTintBinding
import com.inlacou.commons.ui.fragments.BaseFrag
import com.inlacou.inkbetterandroidviews.extensions.itemClicks
import com.inlacou.inkbetterandroidviews.spinners.BetterSpinner
import com.inlacou.inker.Inker
import timber.log.Timber
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

class GradientTintFrag : BaseFrag() {

	private var binder: FragmentGradientTintBinding? = null
	override val title: String? = null

	private val colorPicker1: ColorPicker? get() = binder?.colorPicker1
	private val valueBar1: SaturationValueBar? get() = binder?.valueBar1
	private val saturationBar1: SaturationValueBar? get() = binder?.saturationBar1

	private val colorPicker2: ColorPicker? get() = binder?.colorPicker2
	private val valueBar2: SaturationValueBar? get() = binder?.valueBar2
	private val saturationBar2: SaturationValueBar? get() = binder?.saturationBar2

	private val iv1: ImageView? get() = binder?.iv1
	private val iv2: ImageView? get() = binder?.iv2
	private val beOptions: BetterSpinner? get() = binder?.beOptions

	companion object {
		fun create(model: GradientTintFragMdl): GradientTintFrag
			= GradientTintFrag().apply { arguments = setArgs(model) }
		private fun setArgs(model: GradientTintFragMdl): Bundle = Bundle().apply {
			putInt("color1", model.color1)
			putInt("color2", model.color2)
			putString("algorithm", model.algorithm::class.jvmName)
		}
		internal fun getArgs(bundle: Bundle): GradientTintFragMdl
			= GradientTintFragMdl(
				color1 = bundle.getInt("color1"),
				color2 = bundle.getInt("color2"),
				algorithm = (Class.forName(bundle.getString("algorithm")).kotlin).let {
					it as KClass<out Gradienter>
				}.let {
					it.objectInstance!!
				}
			)
	}

	private val controller: GradientTintFragCtrl by lazy { baseController as GradientTintFragCtrl }
	private lateinit var model: GradientTintFragMdl

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		Timber.d("onCreateView")
		val rootView = inflater.inflate(R.layout.fragment_gradient_tint, container, false)
		binder = FragmentGradientTintBinding.bind(rootView)

		model = getArgs(requireArguments())

		initialize(rootView, savedInstanceState)

		populate(rootView)

		setListeners()

		return rootView
	}

	protected fun initialize(rootView: View, savedInstanceState: Bundle?) {
		super.initialize(rootView)
		baseController = GradientTintFragCtrl(view = this, model = model)
	}

	fun populate(rootView: View? = null) {
		colorPicker1?.addSaturationBar(saturationBar1)
		colorPicker1?.addValueBar(valueBar1)
		colorPicker1?.showCenter = true
		colorPicker1?.showOldCenterColor = false
		colorPicker1?.initializeColor(model.color1, ColorPicker.SOURCE_OUTSIDE)

		colorPicker2?.addSaturationBar(saturationBar2)
		colorPicker2?.addValueBar(valueBar2)
		colorPicker2?.showCenter = true
		colorPicker2?.showOldCenterColor = false
		colorPicker2?.initializeColor(model.color2, ColorPicker.SOURCE_OUTSIDE)
	}

	private fun setListeners() {
		colorPicker1?.onColorChangedListener = ColorPicker.OnColorChangedListener { controller.onColor1Change(it) }
		colorPicker2?.onColorChangedListener = ColorPicker.OnColorChangedListener { controller.onColor2Change(it) }
		disposables.add(beOptions?.itemClicks()?.distinctUntilChanged()?.subscribe({ controller.onOptionSelected(it.first) }, { Inker.e { it }}))
	}

	internal fun getBitmap1() = iv1?.drawable?.toBitmap()
	internal fun setBitmap1(bitmap: Bitmap) = iv1?.setImageBitmap(bitmap)
	internal fun getBitmap2() = iv2?.drawable?.toBitmap()
	internal fun setBitmap2(bitmap: Bitmap) = iv2?.setImageBitmap(bitmap)
	fun setOptions(options: List<String>) {
		beOptions?.setSimpleAdapter(options)
	}
	fun setCurrentOption(option: String) {
		beOptions?.setText(option)
	}
}