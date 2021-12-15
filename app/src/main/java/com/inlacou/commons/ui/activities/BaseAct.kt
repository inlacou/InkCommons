package com.inlacou.commons.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.inlacou.commons.R
import com.inlacou.inkandroidextensions.getView
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Created by inlacoubyv on 26/11/15.
 */
abstract class BaseAct : AppCompatActivity() {

	protected var title: String = ""
	set(value) {
		field = value
		actionBar?.title = value
		supportActionBar?.title = value
	}
    var unknownErrorDialog: Snackbar? = null
    var disposables: MutableList<Disposable?> = mutableListOf()
	protected lateinit var baseController: BaseActCtrl

    protected open fun initialize(savedInstanceState: Bundle? = null) {
	    this.getView()?.let { unknownErrorDialog = Snackbar.make(it, getString(R.string.Unknown_error_happened), Snackbar.LENGTH_LONG) }
	    configureActionBar()
    }

	private fun configureActionBar() {
		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		if (toolbar != null) {
			setSupportActionBar(toolbar)
			supportActionBar?.title = title
		}
	}

	private fun overrideHamburgerIcon(drawableResId: Int){
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setHomeButtonEnabled(true)
		supportActionBar?.setHomeAsUpIndicator(drawableResId)
	}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.none, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
	    var handled = false
	    supportFragmentManager.fragments.forEach { if(it.onOptionsItemSelected(item)) handled = true }
	    return if(!handled){
		    when (item.itemId) {
			    android.R.id.home -> {
				    // app icon in action bar clicked; goto parent activity.
				    this.finish()
				    true
			    }
			    else -> super.onOptionsItemSelected(item)
		    }
	    } else true
    }

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		supportFragmentManager.fragments.forEach { it.onActivityResult(requestCode, resultCode, data) }
		baseController.onActivityResult(requestCode, resultCode, data)
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		supportFragmentManager.fragments.forEach { it.onRequestPermissionsResult(requestCode, permissions, grantResults) }
		baseController.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
		super.onCreate(savedInstanceState, persistentState)
		baseController.onCreate()
	}

	override fun onStart() {
		super.onStart()
		baseController.onStart()
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

	// Keyboard mostly
	private var viewTreeListenersAttached = false
	private var keypadHeight: Int? = null
	private var lastKeypadHeight: Int? = null

	private val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
		//General
		onGlobalLayoutListenerFired()

		/* Keyboard */
		val measureRect = Rect()
		getView()?.getWindowVisibleDisplayFrame(measureRect)
		val actualKeypadHeight = (getView()?.rootView?.height?:0) - measureRect.bottom

		if(keypadHeight==null) {
			keypadHeight = actualKeypadHeight
			if(lastKeypadHeight==null) lastKeypadHeight = actualKeypadHeight
		}else{
			if(lastKeypadHeight!=actualKeypadHeight) {
				if(actualKeypadHeight<keypadHeight?:-1) keypadHeight = actualKeypadHeight
				//val broadcastManager = LocalBroadcastManager.getInstance(this@BaseAct)
				if (actualKeypadHeight > keypadHeight ?: -1) {
					onShowKeyboard()
					val intent = Intent("KeyboardWillShow")
					//broadcastManager.sendBroadcast(intent)
				} else {
					onHideKeyboard()
					val intent = Intent("KeyboardWillHide")
					//broadcastManager.sendBroadcast(intent)
				}
				lastKeypadHeight = actualKeypadHeight
			}
		}
		/* /Keyboard */
	}

	protected open fun onShowKeyboard() {}
	protected open fun onHideKeyboard() {}

	/**
	 * Called when ViewTree changes. For example, keyboard is shown or new items are loaded from server on a recyclerView
	 * Shares origin with onShowKeyboard and onHideKeyboard, but they are not always called. This is.
	 */
	protected open fun onGlobalLayoutListenerFired() {}

	protected fun attachKeyboardListeners() {
		if (viewTreeListenersAttached) return
		getView()?.viewTreeObserver?.addOnGlobalLayoutListener(globalLayoutListener)
		viewTreeListenersAttached = true
	}
	/// Keyboard mostly

	override fun onDestroy() {
		if (viewTreeListenersAttached) {
			getView()?.viewTreeObserver?.removeOnGlobalLayoutListener(globalLayoutListener)
		}
	    baseController.onDestroy()
	    disposables.forEach { it?.dispose() }
        super.onDestroy()
    }

	fun setSubtitle(s: String) {
		runOnUiThread {
			supportActionBar?.subtitle = s
			actionBar?.subtitle = s
		}
	}

	fun finishSuccessfully() {
		setResult(Activity.RESULT_OK)
		finish()
	}
}
