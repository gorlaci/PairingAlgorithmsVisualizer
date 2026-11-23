package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.DARK_GREEN
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.favorite
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BipartiteGraphMatrix(
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
                    GraphicalVertexCell(vertex = vertex, modifier = Modifier.width(40.dp))
                }
            }
        }
        items(class2.size) { index ->
            val rowVertex = class2[index]
            Row {
                GraphicalVertexCell(vertex = rowVertex, modifier = Modifier.width(40.dp))
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
                    BipartiteEdgeCell(
                        edge = edge,
                        modifier = if (rowVertex.highlight.elementAtOrNull(0) == DARK_GREEN ||
                            columnVertex.highlight.elementAtOrNull(0) == DARK_GREEN
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
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black,
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
            imageVector = favorite,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier.size(16.dp),
        )

        if (selected) {
            Icon(
                imageVector = favorite,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(10.dp),
            )
        }
    }
}

@Composable
fun BipartiteEdgeCell(
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
fun GraphicalVertexCell(
    vertex: GraphicalVertex,
    modifier: Modifier = Modifier,
) {
    TextCell(
        text = vertex.name,
        modifier = modifier.background(vertex.highlight.elementAtOrElse(0) { Color.Transparent }),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun TextFieldCell(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val textFieldState = rememberTextFieldState(initialText = value)
    val currentValue by rememberUpdatedState(value)
    val currentOnValueChange by rememberUpdatedState(onValueChange)

    LaunchedEffect(value, textFieldState) {
        if (textFieldState.text.toString() != value) {
            textFieldState.edit {
                replace(0, length, value)
            }
        }
    }

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .collectLatest { text ->
                if (text != currentValue) {
                    currentOnValueChange(text)
                }
            }
    }

    BasicTextField(
        state = textFieldState,
        modifier = modifier.border(1.dp, Color.Black).widthIn(max = 30.dp),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        decorator = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                innerTextField()
            }
        },
        enabled = enabled,
    )
}
