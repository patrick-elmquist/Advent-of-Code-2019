package util

import extension.f
import extension.pow
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

data class Point(val x: Int = 0, val y: Int = 0) {
    val neighbours by lazy {
        listOf(
            copy(x = x - 1),
            copy(x = x + 1),
            copy(y = y - 1),
            copy(y = y + 1)
        )
    }

    constructor(x: Long, y: Long): this(x.toInt(), y.toInt())
    constructor(xy: Pair<Int, Int>): this(xy.first, xy.second)

    fun toPointF() = PointF(x.f, y.f)
}

data class PointF(val x: Float = 0f, val y: Float = 0f) {
    fun toPoint(round: Boolean = false) =
        if (round) Point(x.roundToInt(), y.roundToInt()) else Point(x.toInt(), y.toInt())
}

data class Point3d(var x: Int = 0, var y: Int = 0, var z: Int = 0) {
    fun translateBy(dx: Int, dy: Int, dz: Int): Point3d = copy(x = x + dx, y = y + dy, z = z + dz)
}

fun Point.angleTo(target: Point) = toPointF().angleTo(target.toPointF())
fun PointF.angleTo(target: PointF) = atan2(y - target.y, x - target.x) * 180f / PI.f

fun Point.distanceTo(target: Point) = toPointF().distanceTo(target.toPointF())
fun PointF.distanceTo(target: PointF) = sqrt((x - target.x).pow(2) + (y - target.y).pow(2))