package com.inlacou.inkkotlinextensions.rx

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

object EventBusChannel {

    private val subject: Subject<Event> = PublishSubject.create()
    var stickies: HashMap<Class<out Event>, Event> = hashMapOf()

    val obs get() = subject as Observable<Event>
    inline fun <reified T: Event> filteredObs(sticky: Boolean): Observable<T> {
        if(sticky) {
            val stickyEvent = getStickyEvent<T>()
            if(stickyEvent!=null) return obs.filterIsInstance<T>().startWithItem(stickyEvent)
        }
        return obs.filterIsInstance()
    }

    fun post(item: Event) {
        subject.onNext(item)
    }

    fun postSticky(item: Event, permanent: Boolean = false) {
        stickies[item::class.java] = item
        if(permanent) savePermanent?.invoke(item)
        post(item)
    }

    var savePermanent: ((Event) -> Unit)? = null
    var loadPermanent: ((Class<out Event>) -> Event?)? = null
    var removePermanent: ((Class<out Event>) -> Unit)? = null

    inline fun <reified T: Event> getStickyEvent(permanent: Boolean = false): T? {
        val mem = stickies[T::class.java.classLoader?.loadClass(T::class.java.name)] as T?
        val perm = loadPermanent?.invoke(T::class.java) as T?
        println("DEBUGINLACOU | mem: $mem | perm: $perm")
        return mem ?: if(permanent) perm else null
    }
    /**
     * Alias for [EventBusChannel.getStickyEvent]
     * This overload method does not use the parameter, yeah.
     * It is here to allow the sintax: getStickyEvent(Event::class.java) instead of getStickyEvent<Event>().
     * @see Event
     */
    inline fun <reified T: Event> getStickyEvent(type: Class<T>, permanent: Boolean = false): T? = getStickyEvent(permanent)

    inline fun <reified T: Event> removeStickyEvent() {
        stickies.remove(T::class.java.classLoader?.loadClass(T::class.java.name))
        removePermanent?.invoke(T::class.java)
    }
    /**
     * Alias for [EventBusChannel.removeStickyEvent]
     * This overload method does not use the parameter, yeah.
     * It is here to allow the sintax: removeStickyEvent(Event::class.java) instead of removeStickyEvent<Event>().
     * @see Event
     */
    inline fun <reified T: Event> removeStickyEvent(type: Class<T>) = removeStickyEvent<T>()

    interface Event

}