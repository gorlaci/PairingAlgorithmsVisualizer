package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex

@Composable
fun SearchLegend(modifier: Modifier) {
    val items = listOf(
        "Aktív csúcs" to GraphicalVertex(
            highlight = LIGHT_ORANGE,
        ),
        "Vizsgált csúcs" to GraphicalVertex(
            highlight = LIGHT_YELLOW,
        ),
        "Vizsgált él" to GraphicalEdge(
            startGraphicalVertex = GraphicalVertex(Vector2D(-25.0, -0.0)),
            endGraphicalVertex = GraphicalVertex(Vector2D(25.0, -0.0)),
            highlight = LIGHT_YELLOW,
        ),
        "Faél" to GraphicalEdge(
            startGraphicalVertex = GraphicalVertex(Vector2D(-25.0, -0.0)),
            endGraphicalVertex = GraphicalVertex(Vector2D(25.0, -0.0)),
            selected = true,
        ),
        "Gyökér csúcs" to GraphicalVertex(
            innerColor = LIGHT_PURPLE,
        ),
        "Befejezett gyökér csúcs" to GraphicalVertex(
            innerColor = PURPLE,
        ),
        "Bejárt csúcs" to GraphicalVertex(
            innerColor = LIGHT_PINK,
        ),
        "Befejezett csúcs" to GraphicalVertex(
            innerColor = PINK,
        ),
    )
    Legend(items = items, modifier = modifier)
}
