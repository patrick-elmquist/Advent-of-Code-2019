import util.Day
import java.lang.Math.abs

// Answer #1:
// Answer #2:

fun main() {
    Day(n = 22) {
        answer {
            var cards = (0..10006).toList()
            lines.forEach {
                when {
                    "increment" in it -> {
                        val number = it.split(" ").last().toInt()
                        var index = 0
                        val array = IntArray(cards.size)
                        cards.forEach {
                            array[index] = it
                            index += number
                            index %= (cards.size)
                        }
                        cards = array.toList()
                    }
                    "stack" in it -> cards = cards.reversed()
                    "cut" in it -> {
                        val number = it.split(" ").last().toInt()
                        if (number < 0) {
                            cards = cards.subList(cards.size - abs(number), cards.size) + cards.subList(0, cards.size - abs(number))
                        } else {
                            cards = cards.subList(number, cards.size) + cards.subList(0, number)
                        }
                    }
                }
            }
            println(cards.indexOf(2019))
            ""
        }
        answer {
        }
    }
}

