package week1

import util.Day

// Answer #1: 475
// Answer #2: 297

fun main(args: Array<String>) {
    Day(n = 4) {
        answer {
            val input = lines.first().split("-")
            (input[0].toInt()..input[1].toInt()).filter { checkRules(it) }.count()
        }
        answer {
            val input = lines.first().split("-")
            (input[0].toInt()..input[1].toInt()).filter { checkRules(it, allowLargerGroups = false) }.count()
        }
    }
}

private fun checkRules(password: Int, allowLargerGroups: Boolean = true): Boolean {
    val (_, groups) = password.toString().toByteArray()
        .fold(Pair(Byte.MIN_VALUE, mutableMapOf<Byte, Int>())) { (previous, groups), byte ->
            if (byte < previous) return false
            if (byte == previous) groups[byte] = (groups[byte] ?: 1) + 1
            Pair(byte, groups)
        }
    return groups.isNotEmpty() && allowLargerGroups || groups.values.any { it == 2 }
}
