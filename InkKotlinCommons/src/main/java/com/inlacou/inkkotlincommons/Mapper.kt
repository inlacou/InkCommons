package com.inlacou.inkkotlincommons

/**
 * Interface for model mappers.
 *
 * @param <From>
 * @param <To>
 */
interface Mapper<From, To> {
    fun mapFromTo(type: From): To
    fun mapToFrom(type: To): From
}