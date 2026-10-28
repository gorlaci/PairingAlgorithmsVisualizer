package hu.gorlaci.pairingalgorithmsvisualizer.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.rememberTextMeasurer
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_RED
import hu.gorlaci.pairingalgorithmsvisualizer.ui.drawDirectedEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.drawVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex

@Composable
fun TestScreen() {
    val vertexA = GraphicalVertex(
        name = "A",
        x = -100.0,
        y = 100.0,
    )

    val vertexB = GraphicalVertex(
        name = "B",
        x = 100.0,
        y = 100.0,
    )

    val vertexD = GraphicalVertex(
        name = "D",
        x = -100.0,
        y = -100.0,
    )

    val vertexC = GraphicalVertex(
        name = "C",
        x = 100.0,
        y = -100.0,
    )

    val edges = listOf(
        GraphicalEdge(
            startGraphicalVertex = vertexA,
            endGraphicalVertex = vertexB,
        ),
        GraphicalEdge(
            startGraphicalVertex = vertexB,
            endGraphicalVertex = vertexC,
            highlight = LIGHT_RED,
            selected = true,
        ),
        GraphicalEdge(
            startGraphicalVertex = vertexC,
            endGraphicalVertex = vertexD,
            highlight = LIGHT_RED,
        ),
        GraphicalEdge(
            startGraphicalVertex = vertexD,
            endGraphicalVertex = vertexA,
        ),
        GraphicalEdge(
            startGraphicalVertex = vertexB,
            endGraphicalVertex = vertexA,
        ),
        GraphicalEdge(
            startGraphicalVertex = vertexA,
            endGraphicalVertex = vertexC,
        ),
        GraphicalEdge(
            startGraphicalVertex = vertexC,
            endGraphicalVertex = vertexA,
        ),
    )

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val canvasCenterX = size.width / 2.0
        val canvasCenterY = size.height / 2.0
        val centerOffset = Offset(canvasCenterX.toFloat(), canvasCenterY.toFloat())

        edges.forEach {
            drawDirectedEdge(it, canvasCenterX, canvasCenterY)
        }

        drawVertex(
            vertex = vertexA,
            centerOffset,
            textMeasurer,
        )

        drawVertex(
            vertex = vertexB,
            centerOffset,
            textMeasurer,
        )

        drawVertex(
            vertex = vertexC,
            centerOffset,
            textMeasurer,
        )

        drawVertex(
            vertex = vertexD,
            centerOffset,
            textMeasurer,
        )
    }
}
