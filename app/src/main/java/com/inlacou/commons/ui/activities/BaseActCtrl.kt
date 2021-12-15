package com.inlacou.commons.ui.activities

import android.content.Intent
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Created by inlacou on 19/03/18.
 */
abstract class BaseActCtrl(val _view: BaseAct, val _model: Any) {
	val disposables: MutableList<Disposable?>
	get() = _view.disposables

	open fun onCreate() {}
	open fun onStart() {}
	open fun onResume() {}
	open fun onStop() {}
	open fun onPause() {}
	open fun onDestroy() {}

	open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
	open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {}
}