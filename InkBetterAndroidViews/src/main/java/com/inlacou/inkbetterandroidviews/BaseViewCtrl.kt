package com.inlacou.inkbetterandroidviews

import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewCtrl(val _view: BaseView, val _model: Any){
	protected val disposables: MutableList<Disposable?> get() = _view.disposables
	
	open fun onResume() {}
	open fun onStart() {}
	open fun onPause() {}
	open fun onStop() {}
	open fun onDestroy() {}
	
}