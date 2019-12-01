package extension

fun answer(n: Int, block: () -> Any) = println("Answer #$n: ${ block() }")

fun <T: Number> T.print() = this.also { println(this) }
fun <T: Number> T.printAnswer(n: Int) = this.also { println("Answer #$n: $this") }

fun <T> Collection<T>.println() = forEach { println(it) }