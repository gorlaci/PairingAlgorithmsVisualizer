package hu.gorlaci.pairingalgorithmsvisualizer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.uni.edmonds_algorithm_visualizer.ui.legend.OpenableLegend
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.back_button
import pairingalgorithmsvisualizer.composeapp.generated.resources.next_button
import pairingalgorithmsvisualizer.composeapp.generated.resources.run_button

@Composable
fun AlgorithmRunningScreen(
    title: String,
    selectedGraph: Graph<out Vertex, out Edge>,
    graphList: List<Graph<out Vertex, out Edge>>,
    onGraphIndexSelected: (Int) -> Unit,
    graphicalGraph: GraphicalGraph,
    nextEnabled: Boolean,
    backEnabled: Boolean,
    runEnabled: Boolean,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onRun: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SimpleTopAppbar(
                title = title,
                onBack = onNavigateBack,
            )
        },
    ) { paddingValues ->
        Row(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(0.8f),
            ) {
                GraphSelectionDropdown(
                    selectedGraph = selectedGraph,
                    graphList = graphList,
                    onGraphSelected = onGraphIndexSelected,
                )

                GraphCanvas(
                    graphicalGraph = graphicalGraph,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize(),
            ) {
                OpenableLegend(
                    modifier = Modifier.fillMaxSize().weight(1f),
                )

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        text = graphicalGraph.stepType.description,
                        modifier = Modifier.fillMaxWidth(0.9f),
                    )
                    Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                    Button(
                        onClick = onNext,
                        enabled = nextEnabled,
                    ) {
                        Text(stringResource(Res.string.next_button))
                    }
                    Button(
                        onClick = onBack,
                        enabled = backEnabled,
                    ) {
                        Text(stringResource(Res.string.back_button))
                    }
                    Button(
                        onClick = onRun,
                        enabled = runEnabled,
                    ) {
                        Text(stringResource(Res.string.run_button))
                    }
                }
            }
        }
    }
}