package com.inlacou.inkkotlincommons.reader

import com.inlacou.inkkotlincommons.reader.core.Converter
import com.inlacou.inkkotlincommons.reader.core.Json

class ParentConverter(val childConverter: ChildConverter): Converter {
    override fun convert(json: Json): Json {
        // do something
        return childConverter.convert(json)
    }
}