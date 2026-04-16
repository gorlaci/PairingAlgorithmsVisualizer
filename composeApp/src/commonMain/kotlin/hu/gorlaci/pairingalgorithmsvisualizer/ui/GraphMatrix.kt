package hu.gorlaci.pairingalgorithmsvisualizer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph

@Composable
fun GraphMatrix(
    graphicalGraph: GraphicalGraph,
    class1Ids: Collection<String>,
    class2Ids: Collection<String>,
    modifier: Modifier = Modifier,
) {
    val class1 = graphicalGraph.graphicalVertices.filter { class1Ids.contains(it.label) }
    val class2 = graphicalGraph.graphicalVertices.filter { class2Ids.contains(it.label) }


    LazyColumn(modifier = modifier) {
        item {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                for (vertex in class1) {
                    TextCell(text = vertex.label, modifier = Modifier.weight(1f))
                }
            }
        }
        items(class2.size) { index ->
            val rowVertex = class2[index]
            Row {
                TextCell(text = rowVertex.label, modifier = Modifier.weight(1f))
                for (columnVertex in class1) {
                    val edge = graphicalGraph.graphicalEdges.find {
                        it.startGraphicalVertex == rowVertex && it.endGraphicalVertex == columnVertex
                                || it.startGraphicalVertex == columnVertex && it.endGraphicalVertex == rowVertex
                    }
                    EdgeCell(edge = edge, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TextCell(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.border(1.dp, Color.Black)
    )
}

@Composable
fun EdgeCell(
    edge: GraphicalEdge?,
    modifier: Modifier = Modifier,
) {
    if (edge == null) {
        TextCell(text = "0", modifier = modifier)
    } else {
        Text(
            text = "1",
            modifier = modifier.border(1.dp, Color.Black)
                .background(edge.highlight),
            fontWeight = if (edge.selected) FontWeight.Bold else FontWeight.Normal,
            color = edge.color
        )
    }
}