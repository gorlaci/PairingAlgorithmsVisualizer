package hu.gorlaci.pairingalgorithmsvisualizer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsGraph

@Composable
fun GraphSelectionDropdown(
    selectedGraph: EdmondsGraph,
    graphList: List<EdmondsGraph>,
    onGraphSelected: (Int) -> Unit,
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
