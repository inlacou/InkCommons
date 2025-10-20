package com.inlacou.commons.ui.fragments.coroutines.playground

import com.inlacou.commons.ui.fragments.BaseFragCtrl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CoroutinesPlaygroundFragCtrl(val view: CoroutinesPlaygroundFrag, val model: CoroutinesPlaygroundFragMdl) : BaseFragCtrl(view, model) {

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            coroutineScopeMain.launch(Dispatchers.Main) { view.showError(throwable) }
            GlobalScope.launch {
                //Timber.e("Caught $throwable")
            }
        }

    /**
     * UI Thread
     */
    val coroutineScopeMain: CoroutineScope = CoroutineScope(Dispatchers.Main+coroutineExceptionHandler)
    /**
     * It is backed by a shared pool of threads on JVM. By default,
     * the maximal level of parallelism used by this dispatcher is equal to the number of CPU cores, but is at least two
     */
    val coroutineScopeDefault: CoroutineScope = CoroutineScope(Dispatchers.Default+coroutineExceptionHandler)
    /**
     * is designed for offloading blocking IO tasks to a shared pool of threads.
     * Additional threads in this pool are created and are shutdown on demand.
     * The number of threads used by this dispatcher is limited by the value of
     * "kotlinx.coroutines.io.parallelism" (IO_PARALLELISM_PROPERTY_NAME)
     * system property.
     * It defaults to the limit of 64 threads or the number of cores (whichever is larger).
     */
    val coroutineScopeIO: CoroutineScope = CoroutineScope(Dispatchers.IO+coroutineExceptionHandler)
    /**
     * A coroutine dispatcher that is not confined to any specific thread.
     * It executes initial continuation of the coroutine in the current call-frame and lets the coroutine resume in
     * whatever thread that is used by the corresponding suspending function, without mandating any specific threading policy.
     * Nested coroutines launched in this dispatcher form an event-loop to avoid stack overflows.
     */
    val coroutineScopeUnconfined: CoroutineScope = CoroutineScope(Dispatchers.Unconfined+coroutineExceptionHandler)

    var rxTimerDisposable: Disposable? = null
    val cdisposables = mutableListOf<Job>()

    fun populate() {
        errorHandling()
    }

    override fun onDestroy() {
        cdisposables.forEach { it.cancel("onDestroy") }
        super.onDestroy()
    }

    /* MAIN */

    private fun timers0() {
        //Timber.d("launch timers")
        rxTimer(5)
        coroutineTimer(5)
        //Timber.d("launched timers")
    }

    private fun timers1() {
        //Timber.d("launch timers")
        rxTimer(5)
        coroutineScopeMain.launch { coroutineTimer(5) }
        //Timber.d("launched timers")
    }

    private fun timers2() {
        //Timber.d("launch timers")
        coroutineScopeMain.launch { coroutineTimer(5) }
        rxTimer(5)
        //Timber.d("launched timers")
    }

    private fun callbacksToCoroutines() {
        //Timber.d("about to launch coroutine")
        coroutineScopeMain.launch {
            //Timber.d("result: ${rxCallbackToCoroutine()}")
        }
    }

    private fun fetchFromServerOrDb() {
        //Timber.d("about to get username with Rx")
        fetchUsernameRx {
            //Timber.d("got username with rx (on via callback) '$it'")
        }
        //Timber.d("about to get username with Coroutine")
        cdisposables.add(coroutineScopeMain.launch {
            //Timber.d("about to get username with Coroutine '${fetchUsernameCoroutine()}'")
        })
    }

    private fun errorHandling() {
        //Timber.d("error handling | about to get from server (in 5s)")
        coroutineScopeMain.async {
            //Timber.d("error handling | main | got from server '${fetchUsernameCoroutine()}'")
        }
        coroutineScopeMain.launch { coroutineError("error0 main") }
        coroutineScopeMain.launch { coroutineError("error1 main") }
        coroutineScopeDefault.launch { coroutineError("error0 default") }
        coroutineScopeMain.async {
            //Timber.d("error handling | default | got from server '${fetchUsernameCoroutine()}'")
        }
        coroutineScopeDefault.launch { coroutineError("error1 default") }
        coroutineScopeIO.launch { coroutineError("error0 IO") }
        coroutineScopeIO.launch { coroutineError("error1 IO") }
        coroutineScopeMain.async {
            //Timber.d("error handling | IO | got from server '${fetchUsernameCoroutine()}'")
        }
        coroutineScopeUnconfined.launch { coroutineError("error0 unconfined") }
        coroutineScopeUnconfined.launch { coroutineError("error1 unconfined") }
    }

    /* OTHER */

    private suspend fun normalCallbackToCoroutine(cb: () -> Boolean) {
        suspendCoroutine<Boolean> { it.resume(cb.invoke()) }
    }

    private suspend fun rxCallbackToCoroutine(): Boolean {
        return suspendCoroutine { cont ->
            Observable.timer(5, TimeUnit.SECONDS)
                .subscribe(
                    { cont.resume(true) },
                    { cont.resume(false) })
        } }

    private fun rxTimer(seconds: Int) {
        rxTimerDisposable = Observable.interval(0, 1, TimeUnit.SECONDS).subscribe({
            if(it==seconds.toLong()) {
                rxTimerDisposable?.dispose()
            }
        },{
            //Timber.e("rx timer error $it")
        })
    }

    private fun coroutineTimer(seconds: Int) {
        try {
            repeat(seconds+1) {
                (if(it==seconds) {
                    rxTimerDisposable?.dispose()
                    "coroutine timer up!"
                }else{
                    "coroutine timer ${seconds-it}"
                })
                Thread.sleep(1000)
            }
        }catch (e: Exception) {
            //Timber.e("coroutine timer error $e")
        }
    }

    private fun fetchUsernameRx(cb: (String) -> Unit) {
        Observable.timer(5, TimeUnit.SECONDS).subscribe({ cb.invoke("JoeRx") }, {
            //Timber.e(it)
            }
        )
    }

    private suspend fun fetchUsernameCoroutine(): String {
        Thread.sleep(5000)
        return "JoeCoroutine"
    }

    private suspend fun coroutineError(errorText: String) {
        throw Exception(errorText)
    }

}