package util

import java.io.File

class Day(private val input: Input, block: Day.() -> Unit) {
    private var answerCount: Int = 1
        get() = field.also { field++ }

    constructor(n: Int, block: Day.() -> Unit) :
            this(Input(File("./assets/input-day-$n.txt")), block)

    constructor(input: List<String>, block: Day.() -> Unit) :
            this(Input(input), block)

    constructor(vararg input: String, block: Day.() -> Unit) :
            this(Input(input.asList()), block)

    init {
        block(this)
    }

    fun answer(block: Input.() -> Any) = println("Answer #${answerCount}: ${ block(input) }")
}

class Input(val lines: List<String>) {
    val floats by lazy { lines.map { it.toFloat() } }
    constructor(file: File) : this(file.readLines())
}