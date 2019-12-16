package util

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun sqrt(x: Int) = kotlin.math.sqrt(x.toFloat())

fun lcm(a: Int, b: Int, vararg more: Int): Long =
    lcm(a.toLong(), b.toLong(), *more.map { it.toLong() }.toLongArray())

fun lcm(a: Long, b: Long, vararg more: Long): Long =
    if (more.isEmpty()) {
        lcmInternal(a, b)
    } else {
        mutableListOf(a, b).apply { addAll(more.toList()) }
            .reduce { v1, v2 -> lcmInternal(v1, v2) }
    }

fun lcmInternal(a: Long, b: Long): Long =
    if (a == 0L || b == 0L) 0L else abs(a * b) / gcd(a, b)

tailrec fun gcd(a: Long, b: Long): Long =
    if (a == 0L || b == 0L) {
        a + b
    } else {
        val max = max(abs(a), abs(b))
        val min = min(abs(a), abs(b))
        gcd(max % min, min)
    }