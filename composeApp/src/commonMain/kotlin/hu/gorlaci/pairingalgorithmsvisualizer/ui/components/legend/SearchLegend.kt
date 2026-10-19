package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.ui.GRAY
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_BLUE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.RED
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex

@Composable
fun SearchLegend(modifier: Modifier) {
    val items = listOf(
        "Vizsgált csúcs" to GraphicalVertex(
            highlight = LIGHT_ORANGE,
        ),
        "Vizsgált szomszéd" to GraphicalVertex(
            highlight = LIGHT_BLUE,
        ),
        "Vizsgált él" to GraphicalEdge(
            startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
            endGraphicalVertex = GraphicalVertex(25.0, -0.0),
            highlight = LIGHT_BLUE,
        ),
        "Vizsgálandó csúcs" to GraphicalVertex(
            highlight = GRAY,
        ),
        "Lezárt csúcs" to GraphicalVertex(
            highlight = Color.Black,
        ),
        "Faél" to GraphicalEdge(
            startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
            endGraphicalVertex = GraphicalVertex(25.0, -0.0),
            selected = true,
            color = RED,
        ),
    )
    Legend(items = items, modifier = modifier)
}
