package com.inlacou.inkkotlincommons.monads

interface IWriter {
    var log: String
    val iWriterValue: Any
}

fun <T: IWriter> T.bind(f: (T) -> T): T {
    val current = this.log
    return f(this).apply { log = current + log }
}
fun <T: IWriter> T.append(s: String): T { log = "$s: $iWriterValue\n"; return this }
fun <T: IWriter> T.addWriterStep(s: String): T = append(s)