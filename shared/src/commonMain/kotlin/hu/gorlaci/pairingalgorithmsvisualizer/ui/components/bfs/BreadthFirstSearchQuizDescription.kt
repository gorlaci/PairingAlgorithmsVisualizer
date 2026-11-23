package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.quiz.NeighbourStatus
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz.BreadthFirstSearchQuizScreenViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz.BreadthFirstSearchQuizScreenViewModel.QuestionMode.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.AnswerCard
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.question.Question
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.question.QuestionText

@Composable
fun BreadthFirstSearchQuizDescription(
    viewModel: BreadthFirstSearchQuizScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val questionMode by viewModel.questionMode
    val graphicalGraph by viewModel.graphicalGraph
    val currentGraph by viewModel.currentGraph

    Box(
        modifier = modifier,
    ) {
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
                    onRetry = viewModel::onRetry,
                    modifier = Modifier.padding(5.dp).width(300.dp),
                )
            }

            SELECT_VERTEX -> {
                Question(
                    question = "Melyik csúcs lesz aktív?",
                    answers = currentGraph.vertices.sortedBy { it.name },
                    toString = { it.name },
                    onAnswer = viewModel::onVertexAnswer,
                )
            }

            SELECT_NEIGHBOUR -> {
                Question(
                    question = "Melyik csúcsot vizsgáljuk meg?",
                    answers = currentGraph.vertices.sortedBy { it.name },
                    toString = { it.name },
                    onAnswer = viewModel::onNeighbourAnswer,
                )
            }

            SELECT_NEIGHBOUR_STATUS -> {
                Question(
                    question = "Meg kell-e látogatni ezt a csúcsot (és hozzá venni a vizsgálandó csúcsok listájához)?",
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
    }
}
