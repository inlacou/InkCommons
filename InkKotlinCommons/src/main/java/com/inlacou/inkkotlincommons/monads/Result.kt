package com.inlacou.inkkotlincommons.monads

//TODO make/try Either with this approach
sealed class Result<out T : Any> {
     data class Success<out T : Any>(val data: T) : Result<T>()
     data class Error(val message: String?, val statusCode: Int? = null) : Result<Nothing>()
}