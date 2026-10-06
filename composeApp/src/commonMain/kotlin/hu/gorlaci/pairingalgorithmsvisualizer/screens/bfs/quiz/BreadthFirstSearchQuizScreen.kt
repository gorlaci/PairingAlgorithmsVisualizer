package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.quiz.NeighbourStatus
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchScreenContent
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz.BreadthFirstSearchQuizScreenViewModel.QuestionMode.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.AnswerCard
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.question.Question
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.quiz_screen

@Composable
fun BreadthFirstSearchQuizScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
    onNewGraph: () -> Unit,
) {
    val viewModel = viewModel { BreadthFirstSearchQuizScreenViewModel(graphStorage) }

    val selectedGraph by viewModel.selectedGraph
    val graphicalGraph by viewModel.graphicalGraph
    val quizStarted by viewModel.quizStarted

    val graph by viewModel.currentGraph
    val tree by viewModel.tree
    val inSetup by viewModel.inSetup

    AlgorithmRunningScreen(
        viewModel = viewModel,
        title = stringResource(Res.string.quiz_screen),
        onNavigateBack = onBack,
        onNewGraph = onNewGraph,
        skipButtonsShown = false,
        description = {
            val questionMode by viewModel.questionMode

            when (questionMode) {
                NOTHING -> {
                    Text(
                        text = graphicalGraph.stepType.description,
                        modifier = Modifier.fillMaxWidth(0.9f),
                    )
                }

                SHOW_ANSWER -> {
                    val lastAnswer by viewModel.lastAnswer
                    AnswerCard(
                        answer = lastAnswer,
                        modifier = Modifier.padding(5.dp).width(300.dp),
                    )
                }

                SELECT_VERTEX -> {
                    Question(
                        question = "Melyik csúcs következik a vizsgálandó csúcsok listájában?",
                        answers = graph.vertices.sortedBy { it.name },
                        toString = { it.name },
                        onAnswer = viewModel::onVertexAnswer,
                    )
                }

                SELECT_NEIGHBOUR_STATUS -> {
                    Question(
                        question = "Be kell-e venni a vizsgált szomszédot a listába?",
                        answers = NeighbourStatus.entries,
                        toString = {
                            if (it == NeighbourStatus.ADDED) {
                                "Igen"
                            } else {
                                "Nem"
                            }
                        },
                        onAnswer = viewModel::onNeighbourStatusAnswer,
                    )
                }
            }
        },
    ) {
        BreadthFirstSearchScreenContent(
            viewModel = viewModel,
            graphicalGraph = graphicalGraph,
            tree = tree,
            graph = graph,
            inSetup = inSetup,
        )
    }
}
