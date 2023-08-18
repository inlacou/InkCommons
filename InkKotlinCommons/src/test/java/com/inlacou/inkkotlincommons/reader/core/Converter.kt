package com.inlacou.inkkotlincommons.reader.core

interface Converter {
    fun convert(json: Json): Json
}