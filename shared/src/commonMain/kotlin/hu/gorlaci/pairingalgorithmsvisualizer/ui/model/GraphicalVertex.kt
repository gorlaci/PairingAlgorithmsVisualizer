package hu.gorlaci.pairingalgorithmsvisualizer.ui.model

import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D

data class GraphicalVertex(
    val position: Vector2D = Vector2D(0.0, 0.0),
    val name: String = "",
    val innerColor: Color = Color.White,
    val highlight: List<Color> = emptyList(),
    val highlightType: HighlightType = HighlightType.CIRCLE,
    val label: String = "",
) {

    constructor(
        position: Vector2D = Vector2D(0.0, 0.0),
        name: String = "",
        innerColor: Color = Color.White,
        highlight: Color = Color.Transparent,
        highlightType: HighlightType = HighlightType.CIRCLE,
        label: String = "",
    ) : this(
        position = position,
        name = name,
        innerColor = innerColor,
        highlight = if (highlight == Color.Transparent) emptyList() else listOf(highlight),
        highlightType = highlightType,
        label = label,
    )

    val radius: Float
        get() = 20f + (name.length - 1) * 5f

    val maxTextSize: Int
        get() = (radius * 2).toInt() - (name.length - 1) * 2
}

enum class HighlightType {
    CIRCLE,
    SQUARE,
}
