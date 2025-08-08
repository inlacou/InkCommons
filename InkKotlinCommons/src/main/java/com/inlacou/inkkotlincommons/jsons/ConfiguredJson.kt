package com.inlacou.inkkotlincommons.jsons
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
var ConfiguredJson =
    Json {
        isLenient = true
        ignoreUnknownKeys = true // ideally should be false
        classDiscriminatorMode = ClassDiscriminatorMode.NONE
        encodeDefaults = false
        explicitNulls = false // ideally should be left to true. Requesting Web to put explicit nulls as of 27/06/2024 (Hayk&Inigo).
    }
