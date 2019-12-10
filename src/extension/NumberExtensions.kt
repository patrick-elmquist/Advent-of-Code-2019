package extension

val Number.f get() = this.toFloat()

fun <T: Number> T.print() = this.also { println(this) }