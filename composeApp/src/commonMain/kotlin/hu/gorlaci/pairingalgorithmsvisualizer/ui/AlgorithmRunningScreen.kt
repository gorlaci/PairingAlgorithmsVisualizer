package hu.gorlaci.pairingalgorithmsvisualizer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.run_button

@Composable
fun AlgorithmRunningScreen(
    title: String,
    selectedGraph: Graph<out Vertex, out Edge>,
    graphList: List<Graph<out Vertex, out Edge>>,
    onGraphIndexSelected: (Int) -> Unit,
    graphicalGraph: GraphicalGraph,
    step: Int = 1,
    maxStep: Int,
    nextEnabled: Boolean,
    backEnabled: Boolean,
    runEnabled: Boolean,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onRun: () -> Unit,
    onNavigateBack: () -> Unit,
    onStepChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    legend: @Composable ColumnScope.() -> Unit = { Spacer(modifier = Modifier.height(0.dp)) },
    controls: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {
        GraphCanvas(
            graphicalGraph = graphicalGraph,
            modifier = Modifier.fillMaxSize(),
        )
    }
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
                Row {
                    GraphSelectionDropdown(
                        selectedGraph = selectedGraph,
                        graphList = graphList,
                        onGraphSelected = onGraphIndexSelected,
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
                        modifier = Modifier.fillMaxWidth(0.9f),
                    )
                    Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                    Button(
                        onClick = onRun,
                        enabled = runEnabled,
                    ) {
                        Text(stringResource(Res.string.run_button))
                    }
                    StepSelector(
                        value = step,
                        maxValue = maxStep,
                        onValueChange = onStepChange,
                        onPrevious = onBack,
                        onNext = onNext,
                        previousEnabled = backEnabled,
                        nextEnabled = nextEnabled,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }
        }
    }
}