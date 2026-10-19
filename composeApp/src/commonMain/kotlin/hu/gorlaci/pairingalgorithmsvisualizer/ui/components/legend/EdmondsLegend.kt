package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.HighlightType

@Composable
fun EdmondsLegend(modifier: Modifier = Modifier) {
    val items =
        listOf(
            "Gyökér csúcs" to GraphicalVertex(
                highlight = LIGHT_GREEN,
                highlightType = HighlightType.DOUBLE_CIRCLE,
            ),
            "Külső csúcs" to GraphicalVertex(
                highlight = LIGHT_GREEN,
                highlightType = HighlightType.CIRCLE,
            ),
            "Belső csúcs" to GraphicalVertex(
                highlight = LIGHT_GREEN,
                highlightType = HighlightType.SQUARE,
            ),
            "Párosításbeli él" to GraphicalEdge(
                startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
                endGraphicalVertex = GraphicalVertex(25.0, -0.0),
                selected = true,
            ),
            "Erdő él" to GraphicalEdge(
                startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
                endGraphicalVertex = GraphicalVertex(25.0, -0.0),
                color = LIGHT_GREEN,
            ),
            "Aktuális él" to GraphicalEdge(
                startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
                endGraphicalVertex = GraphicalVertex(25.0, -0.0),
                highlight = LIGHT_YELLOW,
            ),
            "Javító út" to GraphicalEdge(
                startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
                endGraphicalVertex = GraphicalVertex(25.0, -0.0),
                highlight = LIGHT_BLUE,
            ),
            "Kehely" to GraphicalEdge(
                startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
                endGraphicalVertex = GraphicalVertex(25.0, -0.0),
                highlight = LIGHT_PINK,
            ),
            "Már vizsgált él" to GraphicalEdge(
                startGraphicalVertex = GraphicalVertex(-25.0, -0.0),
                endGraphicalVertex = GraphicalVertex(25.0, -0.0),
                highlight = GRAY,
            ),
            "D(G)-ben lévő csúcs" to GraphicalVertex(
                innerColor = LIGHT_BLUE,
            ),
            "A(G)-ben lévő csúcs" to GraphicalVertex(
                innerColor = LIGHT_PINK,
            ),
            "C(G)-ben lévő csúcs" to GraphicalVertex(
                innerColor = LIGHT_GREEN,
            ),
        )
    Legend(
        items = items,
        modifier = modifier,
    )
}
