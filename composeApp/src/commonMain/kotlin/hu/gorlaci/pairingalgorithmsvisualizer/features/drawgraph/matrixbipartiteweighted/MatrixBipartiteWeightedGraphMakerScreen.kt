package hu.gorlaci.pairingalgorithmsvisualizer.features.drawgraph.matrixbipartiteweighted

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.IntInput
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.SimpleTopAppbar
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextCell
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextFieldCell

@Composable
fun MatrixBipartiteWeightedGraphMakerScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val viewmodel = viewModel { MatrixBipartiteWeightedGraphMakerViewmodel(graphStorage) }

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
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(40.dp),
                            )
                        }
                    }
                }
                items(rows) { rowIndex ->
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                    ) {
                        TextCell(
                            text = ('A' + rowIndex).toString(),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(40.dp),
                        )
                        for (columnIndex in 0..<columns) {
                            TextFieldCell(
                                value = if (adjacencyMatrix[rowIndex][columnIndex] == 0) {
                                    ""
                                } else {
                                    adjacencyMatrix[rowIndex][columnIndex].toString()
                                },
                                onValueChange = {
                                    viewmodel.setAdjacencyMatrix(
                                        rowIndex,
                                        columnIndex,
                                        it,
                                    )
                                },
                                modifier = Modifier.width(40.dp).fillMaxHeight(),
                            )
                        }
                    }
                }
            }
        }
    }
}
