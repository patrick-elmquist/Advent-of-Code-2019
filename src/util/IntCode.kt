package util

import extension.asLongs
import extension.csv
import kotlin.math.pow
import kotlin.properties.Delegates

class IntCode(instructions: List<Long>, private val debug: Boolean = false) {
    private val memory = Memory(instructions)

    private var pointer = 0L
    private var relativeBase = 0L

    var hasTerminated = false
        private set

    var output by Delegates.observable(0L) { _, _, value ->
        outputs.add(value).also { log("OUTPUT: $value") }
    }
    val outputs = mutableListOf<Long>()

    fun run(vararg input: Long): Long {
        log("run: ${input.toList()}")
        var inputIndex = 0
        outputs.clear()
        while (true) {
            pointer = when (Op.from(memory[pointer] % 100L)) {
                Op.ADD -> (pointer + 4).also { store(3, paramValue(1) + paramValue(2)) }
                Op.MUL -> (pointer + 4).also { store(3, paramValue(1) * paramValue(2)) }
                Op.IN -> {
                    if (inputIndex < input.size) {
                        (pointer + 2).also { store(1, input[inputIndex++]) }
                    } else {
                        return output
                    }
                }
                Op.OUT -> (pointer + 2).also { output = paramValue(1) }
                Op.JT -> if (paramValue(1) != 0L) paramValue(2) else pointer + 3L
                Op.JF -> if (paramValue(1) == 0L) paramValue(2) else pointer + 3L
                Op.LT -> (pointer + 4).also { store(3, if (paramValue(1) < paramValue(2)) 1 else 0) }
                Op.EQ -> (pointer + 4).also { store(3, if (paramValue(1) == paramValue(2)) 1 else 0) }
                Op.RB -> (pointer + 2).also { relativeBase += paramValue(1) }
                Op.EXIT -> return output.also { hasTerminated = true }
            }
        }
    }

    private fun paramValue(n: Long) =
        when (val mode = mode(memory[pointer] / 100L, n)) {
            0L -> memory[memory[pointer + n]]
            1L -> memory[pointer + n]
            2L -> memory[memory[pointer + n] + relativeBase]
            else -> throw IllegalStateException("Not sure what to do with:$mode")
        }

    private fun store(n: Long, value: Long) =
        when (val mode = mode(memory[pointer] / 100L, n)) {
            0L -> memory[memory[pointer + n]] = value
            2L -> memory[memory[pointer + n] + relativeBase] =  value
            else -> throw IllegalStateException("Not sure what to do with:$mode")
        }

    private fun mode(params: Long, n: Long) = (params / (10f.pow((n - 1L).toFloat()).toLong())) % 10L

    private fun log(msg: String) = if (debug) { println(msg) } else Unit

    private enum class Op(val code: Long) {
        ADD(1), MUL(2), IN(3), OUT(4), JT(5), JF(6), LT(7), EQ(8), RB(9), EXIT(99);
        companion object {
            fun from(code: Long) = values().find { it.code == code }
                ?: throw IllegalStateException("No OP for code: $code")
        }
    }

    private class Memory(instructions: List<Long>) {
        private val memory: MutableMap<Long, Long> = instructions
            .foldIndexed(mutableMapOf()) { i, map, instr -> map.also { map[i.toLong()] = instr } }
        operator fun get(index: Long): Long = memory.getOrPut(index, { 0L })
        operator fun set(index: Long, value: Long) = memory.set(index, value)
    }

    companion object {
        fun verify() {
            check(IntCode(Input(5).lines.first().csv.asLongs()).run(1) == 14155342L)
            check(IntCode(Input(5).lines.first().csv.asLongs()).run(5) == 8684145L)
            check(IntCode("1102,34915192,34915192,7,4,7,99,0".csv.asLongs()).run().toString().length == 16)
            check(IntCode("104,1125899906842624,99".csv.asLongs()).run() == 1125899906842624L)

            val input = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
            check(IntCode(input.csv.asLongs()).apply { run() }.outputs.joinToString(",") == input)

            check(IntCode("109,2000,109,19,204,-34,99".csv.asLongs()).run() == 0L)
            check(IntCode("03,1985,109,2000,109,19,204,-34,99".csv.asLongs()).apply { run(1337) }.output == 1337L)
            check(IntCode("109,2000,203,1,04,2001,99".csv.asLongs()).apply { run(1337) }.output == 1337L)
        }
    }
}
