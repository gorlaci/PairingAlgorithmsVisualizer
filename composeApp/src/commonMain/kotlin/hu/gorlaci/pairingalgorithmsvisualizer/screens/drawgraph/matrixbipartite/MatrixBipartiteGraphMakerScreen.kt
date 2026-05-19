package hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.matrixbipartite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.*

@Composable
fun MatrixBipartiteGraphMakerScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val viewmodel = viewModel { MatrixBipartiteGraphMakerViewmodel(graphStorage) }

    val rows by viewmodel.rows
    val columns by viewmodel.columns
    val adjacencyMatrix by viewmodel.adjacencyMatrix

    val name by viewmodel.name

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextField(
                    value = name,
                    onValueChange = viewmodel::onNameChange,
                    modifier = Modifier.padding(20.dp),
                )

                Button(
                    onClick = viewmodel::saveGraph,
                    modifier = Modifier.padding(20.dp),
                ) {
                    Text(text = "Mentés")
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(30.dp, 15.dp),
            ) {
                Text(
                    text = "Sorok:",
                    fontSize = 18.sp,
                )
                IntInput(
                    value = rows,
                    onValueChange = viewmodel::setRows,
                    minusEnabled = rows > 0,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Oszlopok:",
                    fontSize = 18.sp,
                )
                IntInput(
                    value = columns,
                    onValueChange = viewmodel::setColumns,
                    minusEnabled = columns > 0,
                )
            }
            LazyColumn(
                modifier = Modifier.padding(50.dp),
            ) {
                item {
                    Row {
                        Spacer(modifier = Modifier.width(40.dp))
                        for (i in 0..<columns) {
                            TextCell(
                                text = (i + 1).toString(),
                                modifier = Modifier.width(40.dp),
                            )
                        }
                    }
                }
                items(rows) { rowIndex ->
                    Row {
                        TextCell(
                            text = ('A' + rowIndex).toString(),
                            modifier = Modifier.width(40.dp),
                        )
                        for (columnIndex in 0..<columns) {
                            if (adjacencyMatrix[rowIndex][columnIndex]) {
                                HeartCell(
                                    selected = false,
                                    modifier = Modifier.width(40.dp)
                                        .clickable(
                                            onClick = {
                                                viewmodel.setAdjacencyMatrix(
                                                    rowIndex,
                                                    columnIndex,
                                                    false,
                                                )
                                            },
                                        ),
                                )
                            } else {
                                EmptyCell(
                                    modifier = Modifier.width(40.dp)
                                        .clickable(
                                            onClick = {
                                                viewmodel.setAdjacencyMatrix(
                                                    rowIndex,
                                                    columnIndex,
                                                    true,
                                                )
                                            },
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
