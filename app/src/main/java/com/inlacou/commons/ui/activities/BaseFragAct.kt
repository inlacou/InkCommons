package com.inlacou.commons.ui.activities

import android.content.Intent
import android.view.Menu
import com.inlacou.commons.ui.fragments.BaseFrag

/**
 * Created by inlacoubyv on 26/11/15.
 */
abstract class BaseFragAct : BaseAct() {

	protected var fragment: BaseFrag? = null

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		fragment?.onCreateOptionsMenu(menu, menuInflater)
		return true
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		fragment?.onActivityResult(requestCode, resultCode, data)
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		fragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

}
