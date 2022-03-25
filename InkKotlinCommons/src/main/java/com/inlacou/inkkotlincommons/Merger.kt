package com.inlacou.inkkotlincommons

/**
 * Interface for model mergers.
 *
 * @param <T>
 */
interface Merger<T> {
    fun merge(old: T, new: T): T
    fun merge(list: List<T>, item: T?, onItemNotFoundAdd: Boolean): List<T>
    fun merge(old: List<T>, new: List<T>, onItemNotFoundAdd: Boolean): List<T>
}