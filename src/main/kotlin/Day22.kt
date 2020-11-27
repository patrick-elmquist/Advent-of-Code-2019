import util.Day
import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue

// Answer #1: 7665
// Answer #2:

fun main() {
    Day(n = 22) {
        answer {
            shuffleCardsNaive(10007, lines).indexOf(2019)
        }
        answer {

        }
    }
}

private fun shuffleCardsNaive(cardsInDeck: Int, instructions: List<String>): List<Int> {
    return instructions.fold((0 until cardsInDeck).toList()) { deck, instruction ->
        when {
            "cut" in instruction -> deck.cut(instruction.getNumber())
            "increment" in instruction -> deck.deal(instruction.getNumber())
            "stack" in instruction -> deck.reversed()
            else -> throw IllegalStateException("Should never happen...")
        }
    }
}

private fun List<Int>.cut(n: Int): List<Int> = when {
    n > 0 -> drop(n) + take(n)
    n < 0 -> takeLast(n.absoluteValue) + dropLast(n.absoluteValue)
    else -> this
}

private fun List<Int>.deal(increment: Int): List<Int> {
    return fold(toMutableList() to 0) { (deck, i), card ->
        deck.apply { set(i, card) } to ((i + increment) % size)
    }.first
}

private fun String.getNumber() = this.split(" ").last().toInt()

