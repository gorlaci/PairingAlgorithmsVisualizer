package hu.gorlaci.pairingalgorithmsvisualizer.model.geometry

import androidx.compose.ui.geometry.Offset

data class Vector2D(
    val x: Double,
    val y: Double,
) {
    constructor(offset: Offset) : this(offset.x.toDouble(), offset.y.toDouble())

    operator fun plus(other: Vector2D): Vector2D = Vector2D(x + other.x, y + other.y)

    operator fun minus(other: Vector2D): Vector2D = Vector2D(x - other.x, y - other.y)

    operator fun times(scalar: Double): Vector2D = Vector2D(x * scalar, y * scalar)

    operator fun times(scalar: Int) = times(scalar.toDouble())

    operator fun times(scalar: Float) = times(scalar.toDouble())

    operator fun div(scalar: Double): Vector2D = Vector2D(x / scalar, y / scalar)

    operator fun div(scalar: Int) = div(scalar.toDouble())

    operator fun div(scalar: Float) = div(scalar.toDouble())

    fun rotate(degrees: Double): Vector2D {
        val radians = degrees * (kotlin.math.PI / 180.0)
        val cosAngle = kotlin.math.cos(radians)
        val sinAngle = kotlin.math.sin(radians)
        return Vector2D(
            x * cosAngle - y * sinAngle,
            x * sinAngle + y * cosAngle,
        )
    }

    fun toCanvasCoordinates(canvasCenter: Vector2D): Vector2D = Vector2D(canvasCenter.x + x, canvasCenter.y - y)

    fun length(): Double = kotlin.math.sqrt(x * x + y * y)

    fun toOffset() = Offset(x.toFloat(), y.toFloat())

    fun distanceTo(other: Vector2D) = (this - other).length()
}
