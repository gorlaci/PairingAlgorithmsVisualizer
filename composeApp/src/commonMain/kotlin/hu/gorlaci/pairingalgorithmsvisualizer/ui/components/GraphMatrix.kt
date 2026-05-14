package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.DARK_GREEN
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex

@Composable
fun GraphMatrix(
    graphicalGraph: GraphicalGraph,
    class1Ids: Collection<String>,
    class2Ids: Collection<String>,
    modifier: Modifier = Modifier,
) {
    val class1 = graphicalGraph.graphicalVertices.filter { class1Ids.contains(it.name) }
    val class2 = graphicalGraph.graphicalVertices.filter { class2Ids.contains(it.name) }

    LazyColumn(modifier = modifier.padding(10.dp)) {
        item {
            Row {
                Spacer(modifier = Modifier.width(40.dp))
                for (vertex in class1) {
                    VertexCell(vertex = vertex, modifier = Modifier.width(40.dp))
                }
            }
        }
        items(class2.size) { index ->
            val rowVertex = class2[index]
            Row {
                VertexCell(vertex = rowVertex, modifier = Modifier.width(40.dp))
                for (columnVertex in class1) {
                    val edge = graphicalGraph.graphicalEdges.find {
                        (
                            it.startGraphicalVertex == rowVertex &&
                                it.endGraphicalVertex == columnVertex
                            ) ||
                            (
                                it.startGraphicalVertex == columnVertex &&
                                    it.endGraphicalVertex == rowVertex
                                )
                    }
                    EdgeCell(
                        edge = edge,
                        modifier = if (rowVertex.highlight == DARK_GREEN ||
                            columnVertex.highlight == DARK_GREEN
                        ) {
                            Modifier.width(40.dp).background(DARK_GREEN)
                        } else {
                            Modifier.width(40.dp)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun TextCell(
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.border(1.dp, Color.Black).widthIn(max = 30.dp),
        fontWeight = fontWeight,
        color = color,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun EmptyCell(modifier: Modifier = Modifier) {
    TextCell(text = " ", modifier = modifier)
}

@Composable
fun HeartCell(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        EmptyCell(modifier = Modifier.fillMaxWidth())

        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier.size(16.dp),
        )

        if (selected) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(10.dp),
            )
        }
    }
}

@Composable
fun EdgeCell(
    edge: GraphicalEdge?,
    modifier: Modifier = Modifier,
) {
    if (edge == null) {
        TextCell(text = " ", modifier = modifier)
    } else if (edge.label == null) {
        HeartCell(selected = edge.selected, modifier = modifier.background(edge.highlight))
    } else {
        TextCell(
            text = edge.label,
            modifier = modifier.background(edge.highlight),
            fontWeight = if (edge.selected) FontWeight.ExtraBold else FontWeight.Normal,
            color = edge.color,
        )
    }
}

@Composable
fun VertexCell(
    vertex: GraphicalVertex,
    modifier: Modifier = Modifier,
) {
    TextCell(
        text = vertex.name,
        modifier = modifier.background(vertex.highlight),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun TextFieldCell(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.border(1.dp, Color.Black).widthIn(max = 30.dp),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                innerTextField()
            }
        },
    )
}

@Preview
@Composable
fun SelectedHeartCell() {
    HeartCell(
        selected = true,
        modifier = Modifier.size(30.dp),
    )
}

@Preview
@Composable
fun UnselectedHeartCell() {
    HeartCell(
        selected = false,
        modifier = Modifier.size(30.dp),
    )
}

@Preview
@Composable
fun RowExample() {
    Row {
        VertexCell(vertex = GraphicalVertex(name = "A"), modifier = Modifier.width(40.dp))
        EdgeCell(
            edge = GraphicalEdge(
                startGraphicalVertex = GraphicalVertex(),
                endGraphicalVertex = GraphicalVertex(),
            ),
            modifier = Modifier.width(40.dp),
        )
        HeartCell(selected = true, modifier = Modifier.width(40.dp))
        HeartCell(selected = false, modifier = Modifier.width(40.dp))
    }
}
