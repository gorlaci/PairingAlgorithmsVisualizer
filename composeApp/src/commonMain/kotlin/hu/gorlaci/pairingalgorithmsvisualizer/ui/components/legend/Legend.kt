package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex

@Composable
fun Legend(
    items: List<Pair<String, Any>>,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()

    LazyColumn(
        modifier = modifier,
    ) {
        items.forEach { (text, graphicalElement) ->
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
                                canvasCenter = this.center,
                                textMeasurer = textMeasurer,
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
