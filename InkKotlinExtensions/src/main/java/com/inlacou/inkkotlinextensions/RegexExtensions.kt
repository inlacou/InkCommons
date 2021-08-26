package com.inlacou.inkkotlinextensions

import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.substring(regex: Regex): List<String> {
    val pattern = Pattern.compile(regex.pattern)
    return pattern.matcher(this).results().toList().map { it.group(0) }
}

fun Matcher.results(): List<String> {
    val aux = mutableListOf<String>()
    while(find()) {
        aux.add(group(0) ?: "")
    }
    return aux
}
