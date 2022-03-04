package com.inlacou.inkkotlincommons.monads

class Reader<T, U>(val runReader: (T) -> U) {

    companion object {
        //This is a wrapper over identity function
        fun <T> ask(): Reader<T, T> = Reader { t: T -> t }
    }

    fun <R> local(f: (R) -> T): Reader<R, U> = Reader { r: R -> runReader(f(r)) }
}

//monad
fun <T, Value> Reader.Companion.pure(v: Value): Reader<T, Value> = Reader { v }

fun <T, R, R2> Reader<T, R>.flatMap(transform: (R) -> Reader<T, R2>): Reader<T, R2> = Reader { t: T -> transform(runReader(t)).runReader(t) }

//functor
fun <T, R, R2> Reader<T, R>.map(transform: (R) -> R2): Reader<T, R2> = Reader { t: T -> transform(runReader(t)) }