package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex

@Composable
fun EgervaryLegend(modifier: Modifier = Modifier) {
    val items = listOf(
        "Vizsgálandó A-beli csúcs" to GraphicalVertex(
            highlight = LIGHT_RED,
        ),
        "Vizsgálandó B-beli csúcs" to GraphicalVertex(
            highlight = LIGHT_BLUE,
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
        "e=(u,v) él, amire c(u)+c(v)=w(e)" to GraphicalEdge(
            startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
            endGraphicalVertex = GraphicalVertex(25.0, -0.0),
            color = RED,
        ),
        "Javító út" to GraphicalEdge(
            startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
            endGraphicalVertex = GraphicalVertex(25.0, -0.0),
            highlight = LIGHT_YELLOW,
        ),
        "U-beli csúcs" to GraphicalVertex(
            innerColor = RED,
        ),
        "T'-beli csúcs" to GraphicalVertex(
            innerColor = BLUE,
        ),
        "T-beli csúcs" to GraphicalVertex(
            innerColor = PURPLE,
        ),
        "Él, amin δ felveszi a minimális értékét" to GraphicalEdge(
            startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
            endGraphicalVertex = GraphicalVertex(25.0, -0.0),
            highlight = LIGHT_PINK,
        ),
    )
    Legend(items = items, modifier = modifier)
}
