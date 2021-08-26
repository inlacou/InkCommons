package com.inlacou.inkkotlinextensions.extensions

import com.inlacou.inkkotlinextensions.*
import org.junit.jupiter.api.Test

class RegexExtensions {

    @Test fun `1+1 is 2`() = assert(1+1==2) { "1+1 should equal 2" } /*Just for sanity purposes*/
    @Test fun `version code match | test-1-0-1 dot apk is 1-0-1`() =
            assert("test-1-0-1.apk".substring(Regex("(\\d-?)+"))==listOf("1-0-1")) {
                "${"test-1-0-1.apk".substring(Regex("(\\d-?)+"))} vs ${listOf("1-0-1")}"
            }
    @Test fun `version code match | 1-1-test-1-0-1 dot apk is 1-1- and 1-0-0`() =
            assert("1-1-test-1-0-1.apk".substring(Regex("(\\d-?)+"))==listOf("1-1-", "1-0-1")) {
                "${"1-1-test-1-0-1.apk".substring(Regex("(\\d-?)+"))} vs ${listOf("1-1-", "1-0-1")}"
            }

}