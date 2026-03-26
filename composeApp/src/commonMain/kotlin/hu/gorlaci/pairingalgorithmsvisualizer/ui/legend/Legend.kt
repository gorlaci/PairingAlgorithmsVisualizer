package hu.gorlaci.pairingalgorithmsvisualizer.ui.legend

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.HighlightType

@Composable
fun Legend(
    legends: Map<String, Any>,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier,
    ) {
        legends.forEach { (text, graphicalElement) ->
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (graphicalElement is GraphicalVertex) {
                        Canvas(
                            modifier = Modifier.size(50.dp),
                        ) {
                            drawVertex(
                                vertex = graphicalElement,
                                center = this.center,
                            )
                        }
                    } else if (graphicalElement is GraphicalEdge) {
                        Canvas(
                            modifier = Modifier.size(50.dp),
                        ) {
                            drawEdge(
                                edge = graphicalElement,
                                centerX = this.center.x.toDouble(),
                                centerY = this.center.y.toDouble(),
                            )
                        }
                    }

                    LegendText(text)
                }
            }
        }
    }
}

@Composable
fun EdmondsLegend(modifier: Modifier = Modifier) {
    val legends =
        mapOf(
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
                innerColor = LIGHT_BLUE
            ),
            "A(G)-ben lévő csúcs" to GraphicalVertex(
                innerColor = LIGHT_PINK
            ),
            "C(G)-ben lévő csúcs" to GraphicalVertex(
                innerColor = LIGHT_GREEN,
            )
        )
    Legend(
        legends = legends,
        modifier = modifier,
    )
}

@Composable
fun AugmentingLegend(modifier: Modifier = Modifier) {
    val legends = mapOf(
        "Az A osztályba tartozó csúcs" to GraphicalVertex(
            innerColor = LIGHT_BLUE
        ),
        "A B osztályba tartozó csúcs" to GraphicalVertex(
            innerColor = LIGHT_RED
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
            highlight = LIGHT_ORANGE
        ),
        "Már vizsgált csúcs" to GraphicalVertex(
            highlight = GRAY
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
        )
    )
    Legend(legends = legends, modifier = modifier)
}
