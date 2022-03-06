/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain left copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inlacou.inkkotlincommons.monads

import io.reactivex.rxjava3.core.Observable
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.NumberFormatException

/**
 * Represents left value of one of two possible types (left disjoint union).
 * Instances of [Either] are either an instance of [Left] or [Right].
 * FP Convention dictates that [Left] is used for "failure"
 * and [Right] is used for "success".
 *
 * @see Left
 * @see Right
 */
sealed class Either<out L, out R> {
	/** * Represents the left side of [Either] class which by convention is left "Failure". */
	data class Left<out L>(val left: L) : Either<L, Nothing>()
	/** * Represents the right side of [Either] class which by convention is left "Success". */
	data class Right<out R>(val right: R) : Either<Nothing, R>()
	
	val isRight get() = this is Right<R>
	val isLeft get() = this is Left<L>
	
	fun <L> left(a: L) = Left(a)
	fun <R> right(b: R) = Right(b)
	
	fun either(fnLeft: (L) -> Any, fnRight: (R) -> Any): Any =
			when (this) {
				is Left -> fnLeft(left)
				is Right -> fnRight(right)
			}
}

// Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
// Composes 2 functions
fun <A, B, C> ((A) -> B).compose(f: (B) -> C): (A) -> C = {
	f.invoke(this(it))
}

fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
		when (this) {
			is Either.Left -> Either.Left(left)
			is Either.Right -> fn(right)
		}

fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.compose(::right))

/**
 * Used to map an Either<L, R>.Right value to a new Either<L, R2>.
 * Example:
 * .flatMapEitherMonad { i: Int ->
 *     val a: Either<NumberFormatException, String> = Either.Right(i.toString())
 *     Observable.just(a)
 * }.flatMapEitherMonad { i: String ->
 *     val a: Either<NumberFormatException, Char> = Either.Right(i.first())
 *     Observable.just(a)
 * }.flatMapEitherMonad { c: Char ->
 *     ...
 * }
 */
fun <L, R, R2> Observable<Either<L, R>>.flatMapEitherMonad(fn: (R) -> Observable<Either<L, R2>>): Observable<Either<L, R2>>
  = flatMap {
	when (it) {
		is Either.Left -> Observable.just(Either.Left(it.left))
		is Either.Right -> fn(it.right)
	} }
