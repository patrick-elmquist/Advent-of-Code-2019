package util

fun <T: Number> T.print() = this.also { println(this) }

fun <T> Collection<T>.println() = forEach { println(it) }