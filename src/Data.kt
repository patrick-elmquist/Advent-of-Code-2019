import java.io.File

class Data(day: Int) {
    private val file = File("./assets/input-day-$day.txt")

    val lines by lazy { file.readLines() }
    val floats by lazy { lines.map { it.toFloat() } }
}