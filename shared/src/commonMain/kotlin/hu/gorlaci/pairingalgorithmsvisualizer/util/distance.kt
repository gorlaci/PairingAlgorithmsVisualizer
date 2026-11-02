package hu.gorlaci.pairingalgorithmsvisualizer.util

import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D
import kotlin.math.sqrt

fun distanceFromSegment(
    point: Vector2D,
    segmentStart: Vector2D,
    segmentEnd: Vector2D,
): Double {
    val segment = segmentEnd - segmentStart
    val pointToStart = point - segmentStart
    val t =
        (pointToStart.x * segment.x + pointToStart.y * segment.y) /
            (segment.x * segment.x + segment.y * segment.y)
    val closest =
        segmentStart + Vector2D(segment.x * t.coerceIn(0.0, 1.0), segment.y * t.coerceIn(0.0, 1.0))
    val dx = point.x - closest.x
    val dy = point.y - closest.y
    return sqrt((dx * dx + dy * dy))
}
