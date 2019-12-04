package util

val String.csv: List<String> get() = this.split(",")

fun <T: Collection<String>> T.asInts() = this.map { it.toInt() }
fun <T: Collection<String>> T.asFloats() = this.map { it.toFloat() }
