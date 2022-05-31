package com.inlacou.commons.ui.fragments.gradienttint

import com.inlacou.commons.ui.fragments.BaseFragCtrl
import com.inlacou.inker.Inker
import com.inlacou.inkkotlinextensions.tap

class GradientTintFragCtrl(val view: GradientTintFrag, val model: GradientTintFragMdl): BaseFragCtrl(view, model) {

	private var algorithms: List<Gradienter> = Gradienter::class.sealedSubclasses.mapNotNull {
		it.objectInstance
	}.tap {
		Inker.d { "Instance: ${it::class.simpleName}" }
	}.toList()

	init {
		update()
		view.setOptions(algorithms.mapNotNull { it::class.simpleName })
		view.setCurrentOption(model.algorithm::class.simpleName!!)
	}
	fun onOptionSelected(index: Int) {
		model.algorithm = algorithms[index]
		update()
	}
	fun onColor1Change(it: Int) {
		model.color1 = it
		update()
	}
	fun onColor2Change(it: Int) {
		model.color2 = it
		update()
	}
	private fun update() {
		view.getBitmap1()?.let {
			view.setBitmap1(model.algorithm(it, model.color1, model.color2))
		}
		view.getBitmap2()?.let {
			view.setBitmap2(model.algorithm(it, model.color1, model.color2))
		}
	}
}