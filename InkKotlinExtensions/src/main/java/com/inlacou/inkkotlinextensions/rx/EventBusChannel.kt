package com.inlacou.inkkotlinextensions.rx

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

object EventBusChannel {

    private val subject: Subject<Event> = PublishSubject.create()
    var stickies: HashMap<Class<Event>, Event> = hashMapOf()

    val obs get() = subject as Observable<Event>
    inline fun <reified T: Event> filteredObs(sticky: Boolean): Observable<T> {
        return if(sticky && getStickyEvent<T>()!=null) obs.filterIsInstance<T>().startWithItem(getStickyEvent<T>())
        else obs.filterIsInstance()
    }

    fun post(item: Event) {
        subject.onNext(item)
    }

    fun postSticky(item: Event) {
        stickies[item::class.java as Class<Event>] = item
        post(item)
    }

    inline fun <reified T: Event> getStickyEvent(): T? = stickies[T::class.java.classLoader?.loadClass(T::class.java.name)] as T?
    inline fun <reified T: Event> getStickyEvent(type: Class<T>): T? = stickies[T::class.java.classLoader?.loadClass(T::class.java.name)] as T?

    inline fun <reified T: Event> removeStickyEvent() = stickies.remove(T::class.java.classLoader?.loadClass(T::class.java.name))
    inline fun <reified T: Event> removeStickyEvent(type: Class<T>) = stickies.remove(T::class.java.classLoader?.loadClass(T::class.java.name))

    interface Event

}