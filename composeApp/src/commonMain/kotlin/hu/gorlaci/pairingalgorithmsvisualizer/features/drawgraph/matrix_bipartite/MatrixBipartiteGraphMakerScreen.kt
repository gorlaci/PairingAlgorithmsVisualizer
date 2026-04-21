package hu.gorlaci.pairingalgorithmsvisualizer.features.drawgraph.matrix_bipartite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.EmptyCell
import hu.gorlaci.pairingalgorithmsvisualizer.ui.HeartCell
import hu.gorlaci.pairingalgorithmsvisualizer.ui.SimpleTopAppbar
import hu.gorlaci.pairingalgorithmsvisualizer.ui.TextCell

@Composable
fun MatrixBipartiteGraphMakerScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val viewmodel = viewModel { MatrixBipartiteGraphMakerViewmodel(graphStorage) }

    val rows by viewmodel.rows
    val columns by viewmodel.columns
    val adjacencyMatrix by viewmodel.adjacencyMatrix

    Scaffold(
        topBar = {
            SimpleTopAppbar(
                title = "Páros gráf megadása",
                onBack = onBack,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            Row {
                Text("Sorok száma:")
                TextField(
                    value = rows.toString(),
                    onValueChange = viewmodel::setRows
                )
            }
            Row {
                Text("Oszlopok száma:")
                TextField(
                    value = columns.toString(),
                    onValueChange = viewmodel::setColumns
                )
            }
            LazyColumn {
                item {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        for (i in 0..<columns) {
                            TextCell(
                                text = (i + 1).toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                items(rows) { rowIndex ->
                    Row {
                        TextCell(
                            text = ('A' + rowIndex).toString(),
                            modifier = Modifier.weight(1f)
                        )
                        for (columnIndex in 0..<columns) {
                            if (adjacencyMatrix[rowIndex][columnIndex]) {
                                HeartCell(
                                    selected = false,
                                    modifier = Modifier.weight(1f)
                                        .clickable(
                                            onClick = {
                                                viewmodel.setAdjacencyMatrix(
                                                    rowIndex,
                                                    columnIndex,
                                                    false
                                                )
                                            }
                                        ),
                                )
                            } else {
                                EmptyCell(
                                    modifier = Modifier.weight(1f)
                                        .clickable(
                                            onClick = {
                                                viewmodel.setAdjacencyMatrix(
                                                    rowIndex,
                                                    columnIndex,
                                                    true
                                                )
                                            }
                                        ),
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}