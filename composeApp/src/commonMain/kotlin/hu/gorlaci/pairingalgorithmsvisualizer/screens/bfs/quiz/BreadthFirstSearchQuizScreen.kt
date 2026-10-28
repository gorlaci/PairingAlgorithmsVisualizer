package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.quiz.NeighbourStatus
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz.BreadthFirstSearchQuizScreenViewModel.QuestionMode.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.OpenableLegend
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.SearchLegend
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.question.Question
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.question.QuestionText
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

    val graphicalGraph by viewModel.graphicalGraph

    val currentGraph by viewModel.currentGraph

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
                onBack = viewModel::onBack,
                nextEnabled = nextEnabled,
                backEnabled = backEnabled,
            )
        },
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
                        question = "Melyik csúcsot vizsgáljuk?",
                        answers = currentGraph.vertices.sortedBy { it.name },
                        toString = { it.name },
                        onAnswer = viewModel::onVertexAnswer,
                    )
                }

                SELECT_NEIGHBOUR -> {
                    Question(
                        question = "Melyik szomszédot vizsgáljuk meg?",
                        answers = currentGraph.vertices.sortedBy { it.name },
                        toString = { it.name },
                        onAnswer = viewModel::onNeighbourAnswer,
                    )
                }

                SELECT_NEIGHBOUR_STATUS -> {
                    Question(
                        question = "Be kell-e venni a vizsgált szomszédot a vizsgálandó csúcsok közé?",
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

                FILL_TABLE -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(0.9f),
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                        ) {
                            QuestionText(
                                text = "Töltsd ki a táblázat megfelelő oszlopát!",
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = viewModel::onFillTableAnswer,
                            ) {
                                Text("Kész!")
                            }
                        }
                    }
                }
            }
        },
    ) {
        val tree by viewModel.tree
        val inSetup by viewModel.inSetup
        val tableEditable by viewModel.tableEditable

        Row(modifier = Modifier.fillMaxSize()) {
            UndirectedGraphCanvas(
                graphicalGraph = graphicalGraph,
                modifier = Modifier.fillMaxSize().weight(1f),
                onTap = viewModel::onTap,
            )
            Column(
                modifier = Modifier.fillMaxSize().weight(1f),
            ) {
                UndirectedGraphCanvas(
                    graphicalGraph = tree,
                    modifier = Modifier.fillMaxSize().weight(2f),
                )

                if (!inSetup) {
                    val cellSizeModifier = Modifier.size(40.dp, 25.dp)
                    val firstCellSizeModifier = Modifier.size(80.dp, 25.dp)

                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f),
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                        ) {
                            Row {
                                Spacer(modifier = firstCellSizeModifier)
                                for (vertex in currentGraph.vertices) {
                                    TextCell(
                                        text = vertex.name,
                                        modifier = cellSizeModifier,
                                    )
                                }
                            }

                            Row {
                                TextCell(
                                    text = "táv(v)",
                                    modifier = firstCellSizeModifier,
                                )

                                val tableDistanceContent by viewModel.tableDistanceContent

                                for (vertex in currentGraph.vertices) {
                                    TextFieldCell(
                                        value = tableDistanceContent[vertex.name] ?: "∞",
                                        onValueChange = {
                                            viewModel.onTableDistanceChanged(vertex, it)
                                        },
                                        modifier = cellSizeModifier,
                                        enabled = tableEditable,
                                    )
                                }
                            }

                            Row {
                                TextCell(
                                    text = "előző(v)",
                                    modifier = firstCellSizeModifier,
                                )
                                val tableParentContent by viewModel.tableParentContent
                                for (vertex in currentGraph.vertices) {
                                    TextFieldCell(
                                        value = tableParentContent[vertex.name] ?: "*",
                                        onValueChange = {
                                            viewModel.onTableParentChanged(vertex, it)
                                        },
                                        modifier = cellSizeModifier,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
