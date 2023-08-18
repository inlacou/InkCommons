package com.inlacou.inkkotlincommons.monads

/**
 * Reader<In, Out> is to provide higher abstraction of operation requires access to the external/shared environment in order to produce the desired output.
 *
 * It helps you abstract away the computation into the very last moment (until runReader function is called). One of the benefit for Reader monad is to make the DI (dependency injection) easy, safe and straightforward.
 *
 * Info: https://github.com/kittinunf/ReaderK
 */
class Reader<In, Out>(val run: (In) -> Out) {

    companion object {
        //This is a wrapper over identity function
        /**
         * [ask] is an identity [Reader].
         * [ask] will return the environment that it passed on so it can be useful, if you want to pass along the input in the chain.
         */
        fun <T> ask(): Reader<T, T> = Reader { t: T -> t }
    }

    fun <R> local(f: (R) -> In): Reader<R, Out> = Reader { r: R -> run(f(r)) }
}

//monad
/**
 * [pure] is a bridge into the [Reader], it basically accept one value with any type and then return that type as an output.
 */
fun <In, Out> Reader.Companion.pure(v: Out): Reader<In, Out> = Reader { v }

//functor
/**
 * map transform Reader of one output to another type of output.
 */
fun <In, Out, Other> Reader<In, Out>.map(transform: (Out) -> Other): Reader<In, Other> = Reader { transform(run(it)) }

/**
 * flatMap is handling the transformation in a similar fashion but the allows to accept (U) -> [Reader]<T, Another> instead.
 */
fun <In, Out, Other> Reader<In, Out>.flatMap(transform: (Out) -> Reader<In, Other>): Reader<In, Other> = Reader { transform(run(it)).run(it) }

fun main() {
    val a = Reader<String, Int> { it.toInt() }
    val aux = 10
    println(a.run(aux.toString()) == aux)
    Reader.Companion.pure<String, Int>(10)
}