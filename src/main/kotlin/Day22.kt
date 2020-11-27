import util.Day
import java.math.BigInteger
import java.math.BigInteger.*
import kotlin.math.absoluteValue

// Answer #1: 7665
// Answer #2: 41653717360577

fun main() {
    Day(n = 22) {
        answer {
            shuffleCardsNaive(10007, lines).indexOf(2019)
        }
        answer {
            findValue(lines)
        }
    }
}

private fun findValue(instructions: List<String>): BigInteger {
    val nbrCards = 119315717514047.toBigInteger()
    val shuffles = 101741582076661.toBigInteger()
    val find = 2020.toBigInteger()

    val mem = arrayOf(ONE, ZERO)
    instructions.reversed().forEach { instruction ->
        when {
            "cut" in instruction -> mem[1] += instruction.getNumber().toBigInteger()
            "increment" in instruction -> {
                instruction.getNumber().toBigInteger().modPow(nbrCards - TWO, nbrCards).also {
                    mem[0] *= it
                    mem[1] *= it
                }
            }
            "stack" in instruction -> {
                mem[0] = mem[0].negate()
                mem[1] = (mem[1].inc()).negate()
            }
        }
    }
    val power = mem[0].modPow(shuffles, nbrCards)
    return ((power * find) + ((mem[1] * (power + nbrCards.dec())) * ((mem[0].dec()).modPow(nbrCards - TWO, nbrCards))))
            .mod(nbrCards)
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
