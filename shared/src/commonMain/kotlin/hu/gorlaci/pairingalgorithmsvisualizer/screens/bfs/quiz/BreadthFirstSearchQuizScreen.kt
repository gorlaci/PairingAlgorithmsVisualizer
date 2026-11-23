package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.DistinctionDialog
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.SimpleStepSelector
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs.BreadthFirstSearchQuizDescription
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs.BreadthFirstSearchQuizVertexTable
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs.BreadthFirstSearchScreenContent
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.OpenableLegend
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.SearchLegend
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.shared.generated.resources.Res
import pairingalgorithmsvisualizer.shared.generated.resources.quiz_screen

@Composable
fun BreadthFirstSearchQuizScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
    onNewGraph: () -> Unit,
) {
    val viewModel = viewModel { BreadthFirstSearchQuizScreenViewModel(graphStorage) }

    val showDialog by viewModel.showDialog

    if (showDialog) {
        DistinctionDialog(
            onDismissRequest = viewModel::onDismissDialog,
        )
    }

    AlgorithmRunningScreen(
        viewModel = viewModel,
        title = stringResource(Res.string.quiz_screen),
        onNavigateBack = onBack,
        onNewGraph = onNewGraph,
        skipButtonsShown = false,
        legend = {
            OpenableLegend {
                SearchLegend(modifier = Modifier.fillMaxWidth(0.9f))
            }
        },
        stepSelector = {
            val nextEnabled by viewModel.nextEnabled
            val backEnabled by viewModel.backEnabled

            SimpleStepSelector(
                onNext = viewModel::onNext,
                onPrevious = viewModel::onBack,
                nextEnabled = nextEnabled,
                previousEnabled = backEnabled,
            )
        },
        description = {
            BreadthFirstSearchQuizDescription(
                viewModel = viewModel,
                modifier = Modifier,
            )
        },
    ) {
        BreadthFirstSearchScreenContent(
            viewModel = viewModel,
            table = {
                BreadthFirstSearchQuizVertexTable(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize().weight(1f),
                )
            },
        )
    }
}
