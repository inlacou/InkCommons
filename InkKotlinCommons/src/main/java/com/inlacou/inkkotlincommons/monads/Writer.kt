package com.inlacou.inkkotlincommons.monads

class Writer<T>(val value: T, s: String) {
    var log = "  ${s.padEnd(17)}: $value\n"
        private set
    fun bind(f: (T) -> Writer<T>): Writer<T> = f(this.value).apply { log = this@Writer.log + this.log }
}
