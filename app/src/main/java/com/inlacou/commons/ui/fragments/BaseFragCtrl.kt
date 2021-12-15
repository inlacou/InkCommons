package com.inlacou.commons.ui.fragments

import android.content.Intent
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseFragCtrl(val _view: BaseFrag, val _model: Any) {

	protected val disposables: MutableList<Disposable?>
		get() = _view.disposables

	open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	}

	open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
	}

	open fun onResume() {
	}

	open fun onStop() {
	}

	open fun onPause() {
	}

	open fun onDestroyView() {
		disposables.forEach { it?.dispose() }
	}

	open fun onDestroy() {
	}

}