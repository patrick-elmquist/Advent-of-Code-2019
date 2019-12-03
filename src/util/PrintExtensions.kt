package util

fun <T: Number> T.print() = this.also { println(this) }

fun <T> List<T>.print(): List<T> = onEach { println(it) }