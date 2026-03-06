package hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.runalgorithm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.GraphSelectionDropdown
import hu.gorlaci.pairingalgorithmsvisualizer.ui.SimpleTopAppbar
import hu.gorlaci.uni.edmonds_algorithm_visualizer.ui.legend.OpenableLegend
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.back_button
import pairingalgorithmsvisualizer.composeapp.generated.resources.next_button
import pairingalgorithmsvisualizer.composeapp.generated.resources.run_button

@Composable
fun AlgorithmRunningScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val viewModel = viewModel { AlgorithmRunningViewModel(graphStorage) }

    val selectedGraph by viewModel.selectedGraph
    val graphicalGraph by viewModel.graphicalGraph

    val nextEnabled by viewModel.nextEnabled
    val backEnabled by viewModel.backEnabled
    val runEnabled by viewModel.runEnabled

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SimpleTopAppbar(
                title = "Javítóutas algoritmus",
                onBack = onBack,
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
                    graphList = viewModel.graphList,
                    onGraphSelected = viewModel::onGraphSelected,
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
                        onClick = { viewModel.onNext() },
                        enabled = nextEnabled,
                    ) {
                        Text(stringResource(Res.string.next_button))
                    }
                    Button(
                        onClick = { viewModel.onBack() },
                        enabled = backEnabled,
                    ) {
                        Text(stringResource(Res.string.back_button))
                    }
                    Button(
                        onClick = { viewModel.onRun() },
                        enabled = runEnabled,
                    ) {
                        Text(stringResource(Res.string.run_button))
                    }
                }
            }
        }
    }
}