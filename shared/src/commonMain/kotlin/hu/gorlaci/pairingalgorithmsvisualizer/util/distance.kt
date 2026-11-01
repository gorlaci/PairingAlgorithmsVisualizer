package hu.gorlaci.pairingalgorithmsvisualizer.util

import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

fun distanceFromSegment(
    point: Offset,
    segmentStart: Offset,
    segmentEnd: Offset,
): Float {
    val segment = segmentEnd - segmentStart
    val pointToStart = point - segmentStart
    val t =
        (pointToStart.x * segment.x + pointToStart.y * segment.y) /
            (segment.x * segment.x + segment.y * segment.y)
    val closest =
        segmentStart + Offset(segment.x * t.coerceIn(0f, 1f), segment.y * t.coerceIn(0f, 1f))
    val dx = point.x - closest.x
    val dy = point.y - closest.y
    return sqrt((dx * dx + dy * dy))
}
