package util

typealias Matrix<T> = Array<Array<T>>

fun <T> Matrix<T>.render(): Matrix<T> {
    println(joinToString("\n") { it.joinToString("") })
    println()
    return this
}

fun List<String>.toMatrix(default: Char = ' ') =
    Array(size) { Array(first().length) { default } }.also { matrix ->
        forEachIndexed { x, row -> row.forEachIndexed { y, c -> matrix[x][y] = c } }
    }