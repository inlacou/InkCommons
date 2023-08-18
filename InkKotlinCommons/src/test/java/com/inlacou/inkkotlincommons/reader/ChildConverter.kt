package com.inlacou.inkkotlincommons.reader

import com.inlacou.inkkotlincommons.reader.core.Converter
import com.inlacou.inkkotlincommons.reader.core.Json

class ChildConverter: Converter {
    override fun convert(json: Json): Json {
        // do something
        return json
    }
}