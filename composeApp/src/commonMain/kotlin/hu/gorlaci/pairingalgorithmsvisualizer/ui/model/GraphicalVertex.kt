package hu.gorlaci.pairingalgorithmsvisualizer.ui.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class GraphicalVertex(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val name: String = "",
    val innerColor: Color = Color.White,
    val highlight: Color = Color.Transparent,
    val highlightType: HighlightType = HighlightType.CIRCLE,
    val label: String? = null,
) {

    fun transformCoordinates(
        offsetX: Double,
        offsetY: Double,
    ) = Offset(
        (offsetX + x).toFloat(),
        (offsetY - y).toFloat(),
    )

    val radiusInFloat: Float
        get() = 20f + (name.length - 1) * 5f

    val maxTextSize: Int
        get() = (radiusInFloat * 2).toInt() - (name.length - 1) * 2
}

enum class HighlightType {
    CIRCLE,
    SQUARE,
    DOUBLE_CIRCLE,
}
