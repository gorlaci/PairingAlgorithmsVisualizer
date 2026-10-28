package hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs.quiz

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
import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.quiz.NeighbourStatus
import hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs.quiz.DepthFirstSearchQuizScreenViewModel.QuestionMode.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.question.Question
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.question.QuestionText
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
    val graph by viewModel.currentGraph
    val tree by viewModel.tree
    val inSetup by viewModel.inSetup

    AlgorithmRunningScreen(
        viewModel = viewModel,
        title = stringResource(Res.string.quiz_screen),
        onNavigateBack = onBack,
        onNewGraph = onNewGraph,
        skipButtonsShown = false,
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

                SELECT_NEIGHBOUR -> Question(
                    question = "Melyik szomszédot vizsgáljuk meg?",
                    answers = graph.vertices.sortedBy { it.name },
                    toString = { it.name },
                    onAnswer = viewModel::onNeighbourAnswer,
                )

                FILL_TABLE -> Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                    ) {
                        QuestionText(
                            text = "Töltsd ki a táblázatban a megfelelő cellá(ka)t!",
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
        },
    ) {
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
                    val tableEditable by viewModel.tableEditable

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
                                for (vertex in graph.vertices) {
                                    TextCell(
                                        text = vertex.name,
                                        modifier = cellSizeModifier,
                                    )
                                }
                            }

                            Row {
                                val cellContents by viewModel.tableReachedContent

                                TextCell(
                                    text = "mszám(v)",
                                    modifier = firstCellSizeModifier,
                                )
                                for (vertex in graph.vertices) {
                                    TextFieldCell(
                                        value = cellContents[vertex.name] ?: "",
                                        onValueChange = { viewModel.onTableReachedChanged(vertex, it) },
                                        modifier = cellSizeModifier,
                                        enabled = tableEditable,
                                    )
                                }
                            }

                            Row {
                                val cellContents by viewModel.tableFinishedContent

                                TextCell(
                                    text = "bszám(v)",
                                    modifier = firstCellSizeModifier,
                                )
                                for (vertex in graph.vertices) {
                                    TextFieldCell(
                                        value = cellContents[vertex.name] ?: "",
                                        onValueChange = { viewModel.onTableFinishedChanged(vertex, it) },
                                        modifier = cellSizeModifier,
                                        enabled = tableEditable,
                                    )
                                }
                            }

                            Row {
                                val cellContents by viewModel.tableParentContent

                                TextCell(
                                    text = "előző(v)",
                                    modifier = firstCellSizeModifier,
                                )
                                for (vertex in graph.vertices) {
                                    TextFieldCell(
                                        value = cellContents[vertex.name] ?: "",
                                        onValueChange = { viewModel.onTableParentChanged(vertex, it) },
                                        modifier = cellSizeModifier,
                                        enabled = tableEditable,
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
