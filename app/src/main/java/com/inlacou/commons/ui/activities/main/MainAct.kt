package com.inlacou.commons.ui.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inlacou.commons.R
import com.inlacou.commons.business.Section
import com.inlacou.commons.business.Section.*
import com.inlacou.commons.databinding.ActivityMainBinding
import com.inlacou.commons.list.adapters.SidebarRvAdapter
import com.inlacou.commons.ui.activities.BaseFragAct
import com.inlacou.commons.ui.fragments.BaseFrag
import com.inlacou.commons.ui.fragments.betterspinner.BetterSpinnerFrag
import com.inlacou.commons.ui.fragments.betterspinner.BetterSpinnerFragMdl
import com.inlacou.commons.ui.fragments.coroutines.playground.CoroutinesPlaygroundFrag
import com.inlacou.commons.ui.fragments.coroutines.playground.CoroutinesPlaygroundFragMdl
import com.inlacou.commons.ui.fragments.coroutines.counter.CoroutinesCounterFrag
import com.inlacou.commons.ui.fragments.dialogs.DialogsFrag
import com.inlacou.commons.ui.fragments.dialogs.DialogsFragMdl
import com.inlacou.commons.ui.fragments.eventbusvsrx.EventBusVsRxFrag
import com.inlacou.commons.ui.fragments.eventbusvsrx.EventBusVsRxFragMdl
import com.inlacou.commons.ui.fragments.general.GeneralFrag
import com.inlacou.commons.ui.fragments.textviewbitmap.TextViewBitmapFrag
import com.inlacou.commons.ui.fragments.textviewbitmap.TextViewBitmapFragMdl
import com.inlacou.commons.ui.views.sidebar.SidebarViewMdl
import com.inlacou.inkkotlinextensions.fromJson
import com.inlacou.inkkotlinextensions.toJson
import com.inlacou.commons.ui.fragments.general.GeneralFragMdl
import com.inlacou.commons.ui.fragments.gradienttint.GradientTintFrag
import com.inlacou.commons.ui.fragments.gradienttint.GradientTintFragMdl
import com.inlacou.commons.ui.fragments.gradienttint.Gradienter
import com.inlacou.inkandroidextensions.getColorCompat
import timber.log.Timber

/**
 * Created by inlacou on 02/02/18.
 */
class MainAct : BaseFragAct(), NavigationView.OnNavigationItemSelectedListener {

	private lateinit var toggle: ActionBarDrawerToggle
	
	var binder: ActivityMainBinding? = null
	val drawerLayout: DrawerLayout? get() = binder?.drawerLayout
	val toolbar: Toolbar? get() = binder?.include?.toolbar
	val navView: NavigationView? get() = binder?.navView
	var rvDrawer: RecyclerView? = null

	//Custom Drawer
	private var adapterDrawer: SidebarRvAdapter? = null
	///Custom Drawer

	private lateinit var model: MainActMdl
	private val controller: MainActCtrl get() = baseController as MainActCtrl

	companion object {
		fun navigate(activity: AppCompatActivity, code: Int, model: MainActMdl = MainActMdl()) {
			activity.startActivityForResult(intent(activity, model), code)
		}

		fun intent(activity: AppCompatActivity, model: MainActMdl = MainActMdl()): Intent {
			return Intent(activity, MainAct::class.java).apply {
				putExtra("model", model.toJson())
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		Timber.d("onCreate")
		super.onCreate(savedInstanceState)
		binder = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

		getIntentData()

		initialize(savedInstanceState)

		populate()

		setListeners()
	}

	private fun getIntentData() {
		model = intent?.extras?.getString("model")?.let { it.fromJson() } ?: MainActMdl()
	}

	override fun initialize(savedInstanceState: Bundle?) {
		super.initialize(savedInstanceState)
		baseController = MainActCtrl(view = this, model = model)
		toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
		rvDrawer = drawerLayout?.findViewById(R.id.rv_buttons)
	}

	private fun populate() {
		toggle.syncState()

		loadSection(model.section)

		if(model.drawerOpenedOnStart) drawerLayout?.openDrawer(GravityCompat.START)

		val drawerMenuItems = mutableListOf<SidebarViewMdl>()
		Section.entries.forEach { drawerMenuItems.add(SidebarViewMdl(it, onClick = {
			Timber.d("adapterDrawer: $adapterDrawer")
			adapterDrawer?.onItemSelected(it); loadSection(it.item) })) }
		adapterDrawer = SidebarRvAdapter(rvDrawer, drawerMenuItems)
		rvDrawer?.adapter = adapterDrawer
		rvDrawer?.isNestedScrollingEnabled = false
		rvDrawer?.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
	}

	private fun setListeners() {
		drawerLayout?.addDrawerListener(toggle)
		navView?.setNavigationItemSelectedListener(this)
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		return true
	}

	fun loadSection(section: Section, extras: String? = null) {
		loadFragment(when (section) {
			GENERAL -> GeneralFrag.create(GeneralFragMdl())
			EVENTBUS_VS_RX -> EventBusVsRxFrag.create(EventBusVsRxFragMdl())
			TEXTVIEW_BITMAP -> TextViewBitmapFrag.create(TextViewBitmapFragMdl())
			BETTER_SPINNER -> BetterSpinnerFrag.create(BetterSpinnerFragMdl())
			DIALOGS -> DialogsFrag.create(DialogsFragMdl())
			COROUTINES_PLAYGROUND -> CoroutinesPlaygroundFrag.create(CoroutinesPlaygroundFragMdl())
			COROUTINES_COUNTER -> CoroutinesCounterFrag.create(0)
			GRADIENT_TINT -> GradientTintFrag.create(
				GradientTintFragMdl(
					getColorCompat(com.inlacou.inkbetterandroidviews.R.color.basic_red),
					getColorCompat(com.inlacou.inkbetterandroidviews.R.color.basic_green),
					Gradienter.LinearTopDown
				)
			)
		}, section, extras)
	}

	private fun loadFragment(fragment: Fragment, section: Section, extras: String? = null){
		this.fragment = fragment
		model.section = section
		toolbar?.title =
			section.mdl.textResId?.let { getString(it) }
				?: section.mdl.text
				?: (if(fragment is BaseFrag && fragment.title != null) fragment.title else null)
				?: fragment::class.simpleName
		if (extras != null && extras.isNotEmpty()) {
			val bundle = Bundle()
			val type = object : TypeToken<Map<String, String>>(){}.type
			val mp = Gson().fromJson<Map<String, String>>(extras, type)
			val it = mp.entries.iterator()
			while (it.hasNext()) {
				val pair = it.next()
				Timber.d("${pair.key}: ${pair.value}")
				bundle.putString(pair.key, pair.value)
				//it.remove() // avoids a ConcurrentModificationException
			}
			fragment.arguments = bundle
		}
		invalidateOptionsMenu()

		// Insert the fragment by replacing any existing fragment
		val fragmentManager = supportFragmentManager
		fragmentManager.beginTransaction()
				.replace(R.id.content_main, fragment)
				.commit()
		drawerLayout?.closeDrawer(GravityCompat.START)
	}

	override fun onBackPressed() {
		drawerLayout.let {
			if (it!=null && it.isDrawerOpen(GravityCompat.START)) {
				drawerLayout?.closeDrawer(GravityCompat.START)
			} else {
				fragment.let {
					if(it is BaseFrag && it.onBackPressed()){
						//Do nothing
					}else{
						finish()
					}
				}
			}
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			android.R.id.home -> {
				drawerLayout?.openDrawer(GravityCompat.START)
				true
			}
			else -> fragment?.onOptionsItemSelected(item) ?: false
		}
	}

}
