package util

val String.csv: List<String> get() = this.split(",")

fun Collection<String>.asInts() = this.map { it.toInt() }
fun Collection<String>.asFloats() = this.map { it.toFloat() }