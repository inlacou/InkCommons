package com.inlacou.commons.ui.fragments

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.inlacou.commons.R
import io.reactivex.rxjava3.disposables.Disposable

/**
 * It's aimed at full view fragments (f.e. for Navigation View Activity)
 */
abstract class BaseFrag : Fragment() {

	open val title: String? = null
	var unknownErrorDialog: Snackbar? = null
	var disposables: MutableList<Disposable?> = mutableListOf()
	protected lateinit var baseController: BaseFragCtrl

	protected fun initialize(rootView: View) {
		activity?.let {
			this.view?.let { unknownErrorDialog = Snackbar.make(it, getString(R.string.Unknown_error_happened), Snackbar.LENGTH_LONG) }
		}
	}

	override fun onDestroyView() {
		baseController.onDestroyView()
		disposables.forEach { it?.dispose() }
		super.onDestroyView()
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		baseController.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		baseController.onActivityResult(requestCode, resultCode, data)
	}

	override fun onResume() {
		super.onResume()
		baseController.onResume()
	}

	override fun onPause() {
		baseController.onPause()
		super.onPause()
	}

	override fun onStop() {
		baseController.onStop()
		super.onStop()
	}

	override fun onDestroy() {
		baseController.onDestroy()
		super.onDestroy()
	}

	/**
	 * @return true if consumed, false if not
	 */
	open fun onBackPressed(): Boolean {
		return false
	}
}