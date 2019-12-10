package util

import java.io.File

class Day(private val input: Input, block: Day.() -> Unit) {
    private var answerCount: Int = 1
        get() = field.also { field++ }

    constructor(n: Int, block: Day.() -> Unit) : this(Input(n), block)

    init {
        block(this)
    }

    fun answer(block: Input.() -> Any?) = answer(input, block)
    fun answer(vararg input: String, block: Input.() -> Any?) = answer(Input(*input), block)

    private fun answer(input: Input, block: Input.() -> Any?) = println("Answer #${answerCount}: ${ block(input) }")
}

class Input(val lines: List<String>) {
    val floats by lazy { lines.asFloats() }

    constructor(day: Int) : this(File("./assets/input-day-$day.txt"))
    constructor(file: File) : this(file.readLines())
    constructor(vararg input: String) : this(input.asList())
}