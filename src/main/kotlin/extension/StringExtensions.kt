package extension

val String.csv: List<String> get() = this.split(",")

fun String.print(): String = this.also { println(this) }

fun String.splitLines() = split("\n")