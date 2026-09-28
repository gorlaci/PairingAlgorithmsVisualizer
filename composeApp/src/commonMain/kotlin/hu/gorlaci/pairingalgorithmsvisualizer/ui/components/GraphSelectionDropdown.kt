package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.new_graph_button

@Composable
fun GraphSelectionDropdown(
    selectedGraph: Graph<out Vertex, out Edge<out Vertex>>,
    graphList: List<Graph<out Vertex, out Edge<out Vertex>>>,
    onGraphSelected: (Int) -> Unit,
    onNewGraph: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val graphSelectionExpanded = remember { mutableStateOf(false) }

    Box(
        modifier = modifier.padding(10.dp),
    ) {
        TextField(
            value = selectedGraph.name,
            onValueChange = { /* Readonly */ },
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { graphSelectionExpanded.value = !graphSelectionExpanded.value },
                ) {
                    Icon(Icons.Default.ArrowDropDown, null)
                }
            },
        )

        DropdownMenu(
            expanded = graphSelectionExpanded.value,
            onDismissRequest = { graphSelectionExpanded.value = false },
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.new_graph_button)) },
                onClick = {
                    onNewGraph()
                    graphSelectionExpanded.value = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(Res.string.new_graph_button),
                    )
                },
            )

            graphList.forEachIndexed { index, graph ->
                DropdownMenuItem(
                    text = { Text(graph.name) },
                    onClick = {
                        onGraphSelected(index)
                        graphSelectionExpanded.value = false
                    },
                )
            }
        }
    }
}
