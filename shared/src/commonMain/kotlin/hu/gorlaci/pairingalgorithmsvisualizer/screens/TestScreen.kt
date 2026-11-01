package hu.gorlaci.pairingalgorithmsvisualizer.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_RED
import hu.gorlaci.pairingalgorithmsvisualizer.ui.drawDirectedEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.drawVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex

@Composable
fun TestScreen() {
    val vertexA = GraphicalVertex(
        name = "A",
        position = Vector2D(-100.0, 100.0),
    )

    val vertexB = GraphicalVertex(
        name = "B",
        position = Vector2D(100.0, 100.0),
    )

    val vertexD = GraphicalVertex(
        name = "D",
        position = Vector2D(-100.0, -100.0),
    )

    val vertexC = GraphicalVertex(
        name = "C",
        position = Vector2D(100.0, -100.0),
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
        val canvasCenter = Vector2D(size.width / 2.0, size.height / 2.0)

        edges.forEach {
            drawDirectedEdge(it, canvasCenter)
        }

        drawVertex(
            vertex = vertexA,
            canvasCenter,
            textMeasurer,
        )

        drawVertex(
            vertex = vertexB,
            canvasCenter,
            textMeasurer,
        )

        drawVertex(
            vertex = vertexC,
            canvasCenter,
            textMeasurer,
        )

        drawVertex(
            vertex = vertexD,
            canvasCenter,
            textMeasurer,
        )
    }
}
