package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphSelectionDropdown
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.SimpleTopAppbar
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.StepSelector
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.run_button

@Composable
fun AlgorithmRunningScreen(
    viewModel: AlgorithmRunningViewModel,
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    legend: @Composable ColumnScope.() -> Unit = { Spacer(modifier = Modifier.height(0.dp)) },
    controls: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {
        GraphCanvas(
            graphicalGraph = viewModel.graphicalGraph.value,
            onTap = viewModel::onTap,
            modifier = Modifier.fillMaxSize(),
        )
    },
) {
    val graphList = viewModel.graphList

    val selectedGraph by viewModel.selectedGraph
    val graphicalGraph by viewModel.graphicalGraph

    val step by viewModel.step
    val maxStep by viewModel.maxStep

    val nextEnabled by viewModel.nextEnabled
    val backEnabled by viewModel.backEnabled
    val runEnabled by viewModel.inSetup
    val skipForwardEnabled by viewModel.skipForwardEnabled
    val skipBackwardEnabled by viewModel.skipBackwardEnabled

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
                Row {
                    GraphSelectionDropdown(
                        selectedGraph = selectedGraph,
                        graphList = graphList,
                        onGraphSelected = viewModel::onGraphSelected,
                    )
                    controls()
                }

                content()
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize(),
            ) {
                legend()

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        text = graphicalGraph.stepType.description,
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                    )
                    Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                    Button(
                        onClick = viewModel::onRun,
                        enabled = runEnabled,
                    ) {
                        Text(stringResource(Res.string.run_button))
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(0.9f),
                    ) {
                        Button(
                            onClick = viewModel::onSkipBackward,
                            enabled = skipBackwardEnabled,
                        ) {
                            Text("Ugrás vissza")
                        }

                        Button(
                            onClick = viewModel::onSkipForward,
                            enabled = skipForwardEnabled,
                        ) {
                            Text("Ugrás előre")
                        }
                    }

                    StepSelector(
                        value = step + 1,
                        maxValue = maxStep,
                        onValueChange = viewModel::onStepChange,
                        onPrevious = viewModel::onBack,
                        onNext = viewModel::onNext,
                        previousEnabled = backEnabled,
                        nextEnabled = nextEnabled,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }
        }
    }
}
