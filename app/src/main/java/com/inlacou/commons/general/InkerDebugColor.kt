package com.inlacou.commons.general
import android.util.Log
import com.inlacou.inker.Inker

/**
 * Configuration for Inker logger library.
 * https://github.com/inlacou/Inker
 */
class InkerDebugColor: Inker.Color() {
	override val v: ((s: String?) -> Unit) =       { Log.v(  try { "ink-"+createTag() }catch (e: Exception) { "INKER_ERROR" }, it ?: "null") }
	override val d: ((s: String?) -> Unit) =       { Log.d(  try { "ink-"+createTag() }catch (e: Exception) { "INKER_ERROR" }, it ?: "null") }
	override val i: ((s: String?) -> Unit) =       { Log.i(  try { "ink-"+createTag() }catch (e: Exception) { "INKER_ERROR" }, it ?: "null") }
	override val w: ((s: String?) -> Unit) =       { Log.w(  try { "ink-"+createTag() }catch (e: Exception) { "INKER_ERROR" }, it ?: "null") }
	override val w2: ((t: Throwable?) -> Unit) =   { Log.w(  try { "ink-"+createTag() }catch (e: Exception) { "INKER_ERROR" }, it) }
	override val e: ((s: String?) -> Unit) =       { Log.e(  try { "ink-"+createTag() }catch (e: Exception) { "INKER_ERROR" }, it ?: "null") }
	override val e2: ((t: Throwable?) -> Unit) =   { Log.e(  try { "ink-"+createTag() }catch (e: Exception) { "INKER_ERROR" }, null, it) }
	override val wtf: ((s: String?) -> Unit) =     { Log.wtf(try { "ink-"+createTag() }catch (e: Exception) { "INKER_ERROR" }, it) }
}