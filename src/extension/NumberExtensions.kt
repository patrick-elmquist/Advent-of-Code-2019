package extension

import kotlin.math.pow

val Number.f get() = this.toFloat()
fun Number.pow(n: Int) = toFloat().pow(n).toInt()

fun <T: Number> T.print() = this.also { println(this) }