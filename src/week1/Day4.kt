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
    val groups = mutableMapOf<Int, Int>()
    var previousDigit = Int.MAX_VALUE
    var remainingPassword = password

    while (remainingPassword > 0) {
        val digit = remainingPassword % 10
        when {
            digit > previousDigit -> return false
            digit == previousDigit -> groups[digit] = (groups[digit] ?: 1) + 1
        }
        previousDigit = digit
        remainingPassword /= 10
    }
    return groups.isNotEmpty() && allowLargerGroups || groups.values.any { it == 2 }
}
