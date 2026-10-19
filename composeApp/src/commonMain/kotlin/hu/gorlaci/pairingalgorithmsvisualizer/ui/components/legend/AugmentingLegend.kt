package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex

@Composable
fun AugmentingLegend(modifier: Modifier = Modifier) {
    val items = listOf(
        "Az A osztályba tartozó csúcs" to GraphicalVertex(
            innerColor = LIGHT_BLUE,
        ),
        "A B osztályba tartozó csúcs" to GraphicalVertex(
            innerColor = LIGHT_RED,
        ),
        "Vizsgálandó A-beli csúcs" to GraphicalVertex(
            innerColor = LIGHT_BLUE,
            highlight = RED,
        ),
        "Vizsgálandó B-beli csúcs" to GraphicalVertex(
            innerColor = LIGHT_RED,
            highlight = BLUE,
        ),
        "Aktuálisan vizsgált csúcs" to GraphicalVertex(
            highlight = LIGHT_ORANGE,
        ),
        "Már vizsgált csúcs" to GraphicalVertex(
            highlight = GRAY,
        ),
        "Párosításbeli él" to GraphicalEdge(
            startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
            endGraphicalVertex = GraphicalVertex(25.0, -0.0),
            selected = true,
        ),
        "Alternáló fa él" to GraphicalEdge(
            startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
            endGraphicalVertex = GraphicalVertex(25.0, -0.0),
            color = DARK_GREEN,
        ),
        "Javító út" to GraphicalEdge(
            startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
            endGraphicalVertex = GraphicalVertex(25.0, -0.0),
            highlight = LIGHT_YELLOW,
        ),
        "Lefogó ponthalmaz" to GraphicalVertex(
            highlight = DARK_GREEN,
        ),
    )
    Legend(items = items, modifier = modifier)
}
