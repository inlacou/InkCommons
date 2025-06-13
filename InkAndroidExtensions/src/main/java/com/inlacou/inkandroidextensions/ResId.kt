package com.inlacou.inkandroidextensions

import android.content.Context

data class ResId(val value: Int) {
    fun getColorCompat(context: Context): Int {
        return context.resources.getColorCompat(value)
    }
}