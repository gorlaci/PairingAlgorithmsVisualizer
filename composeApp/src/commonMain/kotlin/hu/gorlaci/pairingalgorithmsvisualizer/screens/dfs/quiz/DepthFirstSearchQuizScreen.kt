package hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs.quiz

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
import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.quiz.NeighbourStatus
import hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs.DepthFirstSearchScreenContent
import hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs.quiz.DepthFirstSearchQuizScreenViewModel.QuestionMode.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.AnswerCard
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.question.Question
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.quiz_screen

@Composable
fun DepthFirstSearchQuizScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
    onNewGraph: () -> Unit,
) {
    val viewModel = viewModel { DepthFirstSearchQuizScreenViewModel(graphStorage) }
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
                NOTHING -> Text(graphicalGraph.stepType.description, Modifier.fillMaxWidth(0.9f))
                SHOW_ANSWER -> {
                    val lastAnswer by viewModel.lastAnswer
                    AnswerCard(lastAnswer, Modifier.padding(5.dp).width(300.dp))
                }
                SELECT_VERTEX -> Question(
                    question = "Melyik csúcsból folytatódik a bejárás?",
                    answers = graph.vertices.sortedBy { it.name },
                    toString = { it.name },
                    onAnswer = viewModel::onVertexAnswer,
                )
                SELECT_NEIGHBOUR_STATUS -> Question(
                    question = "Be kell-e járni a vizsgált szomszédot?",
                    answers = NeighbourStatus.entries,
                    toString = { if (it == NeighbourStatus.VISIT) "Igen" else "Nem" },
                    onAnswer = viewModel::onNeighbourStatusAnswer,
                )
            }
        },
    ) {
        DepthFirstSearchScreenContent(
            viewModel = viewModel,
            graphicalGraph = graphicalGraph,
            tree = tree,
            graph = graph,
            inSetup = inSetup,
        )
    }
}
