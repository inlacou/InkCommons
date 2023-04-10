package com.inlacou.commons.monads

import com.inlacou.inkkotlincommons.monads.Either
import com.inlacou.inkkotlincommons.monads.flatMap
import com.inlacou.inkkotlincommons.monads.flatMapEitherMonad
import com.inlacou.inkkotlincommons.monads.map
import io.reactivex.rxjava3.core.Observable
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.NumberFormatException

fun main() {
    val either: Either<NullPointerException, Int> = Either.Right(2)

    when(either) {
        is Either.Left -> "left"
        is Either.Right -> "right"
        //No 'else' branch is required
    }.let {
    }

    either.map {
        3
    }.let {
    }

    either.flatMap {
        Either.Right(it.toString())
    }.let {
    }

    either.flatMap {
        val a: Either<NumberFormatException, Int> = Either.Right(2)
        a
    }.let {
    }

    either.flatMap {
        Either.Right(it.toString())
    }.let {
    }

    Observable.just(either)
        .flatMapEitherMonad {
            Observable.just(Either.Right(it.toString()))
        }
        .flatMap {
            Observable.just("")
        }.subscribe({
            println("change right side completely")
        },{})

    (Observable.just(either) as Observable<Either<Exception, Int>>)
        .flatMapEitherMonad { i: Int ->
            val a: Either<NumberFormatException, String> = Either.Right(i.toString())
            Observable.just(a)
        }.flatMapEitherMonad { i: String ->
            val a: Either<NumberFormatException, Char> = Either.Right(i.first())
            Observable.just(a)
        }.subscribe({
            println("change left side (common supertype)")
        },{})
}
