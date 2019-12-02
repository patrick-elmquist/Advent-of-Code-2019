package util

fun <T: Number> T.print() = this.also { println(this) }

fun <T> Collection<T>.print() = forEach { println(it) }