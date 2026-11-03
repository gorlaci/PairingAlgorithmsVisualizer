package hu.gorlaci.pairingalgorithmsvisualizer.ui.model

import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D

data class GraphicalVertex(
    val position: Vector2D = Vector2D(0.0, 0.0),
    val name: String = "",
    val innerColor: Color = Color.White,
    val highlight: Color = Color.Transparent,
    val highlightType: HighlightType = HighlightType.CIRCLE,
    val label: String = "",
) {

    val radius: Float
        get() = 20f + (name.length - 1) * 5f

    val maxTextSize: Int
        get() = (radius * 2).toInt() - (name.length - 1) * 2
}

enum class HighlightType {
    CIRCLE,
    SQUARE,
    DOUBLE_CIRCLE,
}
