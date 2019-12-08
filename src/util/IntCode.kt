package util

import kotlin.math.pow

class IntCode(private val instructions: List<Int>, private val debug: Boolean = false) {
    private val memory = instructions.toMutableList()
    private var pointer: Int = 0
    var output: Int = 0
        private set(value) {
            field = value
            log("OUTPUT: $field")
        }
    var isFinished = false
        private set

    fun run(input: List<Int>): Int {
        log("run: $input")
        var inputIndex = 0

        while (true) {
            pointer = when (val op = Op.from(memory[pointer] % 100)) {
                Op.ADD -> (pointer + 4).also { store(pointer + 3, parameter(1) + parameter(2)) }
                Op.MUL -> (pointer + 4).also { store(pointer + 3, parameter(1) * parameter(2)) }
                Op.IN -> {
                    if (inputIndex >= input.size) {
                        return output
                    } else {
                        (pointer + 2).also { store(pointer + 1, input[inputIndex++]) }
                    }
                }
                Op.OUT -> (pointer + 2).also { output = parameter(1) }
                Op.JT -> if (parameter(1) != 0) parameter(2) else pointer + 3
                Op.JF -> if (parameter(1) == 0) parameter(2) else pointer + 3
                Op.LT -> (pointer + 4).also { store(pointer + 3, if (parameter(1) < parameter(2)) 1 else 0) }
                Op.EQ -> (pointer + 4).also { store(pointer + 3, if (parameter(1) == parameter(2)) 1 else 0) }
                Op.EXIT -> return output.also { isFinished = true }
            }
        }
    }

    private fun parameter(n: Int) = if (mode(memory[pointer] / 100, n) == 0) {
        memory[memory[pointer + n]]
    } else {
        memory[pointer + n]
    }

    private fun store(index: Int, value: Int) {
        memory[memory[index]] = value
    }

    private fun mode(params: Int, n: Int) = (params / (10f.pow(n - 1).toInt())) % 10

    private enum class Op(val code: Int) {
        ADD(1), MUL(2), IN(3), OUT(4), JT(5), JF(6), LT(7), EQ(8), EXIT(99);
        companion object {
            fun from(code: Int) = values().find { it.code == code } ?: throw IllegalStateException("Sink ship")
        }
    }

    private fun log(msg: String) = if (debug) { println(msg) } else Unit
}