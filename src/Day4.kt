import util.Day

// Answer #1: 475
// Answer #2: 297

fun main(args: Array<String>) {
    Day(n = 4) {
        answer {
            val input = lines.first().split("-")
            (input[0].toInt()..input[1].toInt())
                .filter { password -> checkRules(password, allowLargerGroups = true) }
                .count()
        }
        answer {
            val input = lines.first().split("-")
            (input[0].toInt()..input[1].toInt())
                .filter { password -> checkRules(password, allowLargerGroups = false) }
                .count()
        }
    }
}

private fun checkRules(value: Int, allowLargerGroups: Boolean): Boolean {
    val inARow = mutableMapOf<Int, Int>()
    var lastDigit = Int.MAX_VALUE
    var remainingValue = value

    while (remainingValue > 0) {
        val digit = remainingValue % 10
        when {
            digit > lastDigit -> return false
            digit == lastDigit -> inARow[digit] = (inARow[digit] ?: 1) + 1
        }
        lastDigit = digit
        remainingValue /= 10
    }
    return inARow.values.isNotEmpty() && allowLargerGroups || inARow.values.any { it == 2 }
}
