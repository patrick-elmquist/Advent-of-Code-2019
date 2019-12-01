package util

import java.io.File

data class Day(val n: Int, val block: Day.() -> Unit) {
    private val input by lazy { Input(File("./assets/input-day-$n.txt")) }
    private var answerCount: Int = 1
        get() = field.also { field++ }

    init {
        block(this)
    }

    fun answer(block: Input.() -> Any) = println("Answer #${answerCount}: ${ block(input) }")
}

class Input internal constructor(file: File) {
    val lines by lazy { file.readLines() }
    val floats by lazy { lines.map { it.toFloat() } }
}