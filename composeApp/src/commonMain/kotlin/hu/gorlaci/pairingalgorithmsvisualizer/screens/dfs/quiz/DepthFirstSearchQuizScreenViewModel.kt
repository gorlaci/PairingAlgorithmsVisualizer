package hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs.quiz

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.DepthFirstSearchVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.quiz.DepthFirstSearchStepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.quiz.NeighbourStatus
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.Answer
import hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs.DepthFirstSearchAlgorithmRunningViewModel

class DepthFirstSearchQuizScreenViewModel(
    graphStorage: GraphStorage,
) : DepthFirstSearchAlgorithmRunningViewModel(graphStorage) {
    override val nextEnabled = mutableStateOf(false)
    override val backEnabled = mutableStateOf(false)

    val quizStarted = mutableStateOf(false)
    val questionMode = mutableStateOf(QuestionMode.NOTHING)
    val lastAnswer: MutableState<Answer> = mutableStateOf(Answer.Correct)

    val tableEditable = mutableStateOf(false)
    val tableReachedContent = mutableStateOf(
        currentGraph.value.vertices.associate {
            it.name to
                (it.reachedNumber?.toString() ?: "")
        },
    )
    val tableFinishedContent = mutableStateOf(
        currentGraph.value.vertices.associate {
            it.name to
                (it.finishedNumber?.toString() ?: "")
        },
    )
    val tableParentContent =
        mutableStateOf(currentGraph.value.vertices.associate { it.name to (it.parent?.name ?: "*") })

    fun onTableReachedChanged(
        vertex: DepthFirstSearchVertex,
        value: String,
    ) {
        tableReachedContent.value = tableReachedContent.value.toMutableMap().apply {
            this[vertex.name] = value.trim()
        }
    }

    fun onTableFinishedChanged(
        vertex: DepthFirstSearchVertex,
        value: String,
    ) {
        tableFinishedContent.value = tableFinishedContent.value.toMutableMap().apply {
            this[vertex.name] = value.trim()
        }
    }

    fun onTableParentChanged(
        vertex: DepthFirstSearchVertex,
        value: String,
    ) {
        tableParentContent.value = tableParentContent.value.toMutableMap().apply {
            this[vertex.name] = value.trim()
        }
    }

    override fun onGraphSelected(index: Int) {
        quizStarted.value = false
        questionMode.value = QuestionMode.NOTHING
        super.onGraphSelected(index)
        setButtons()
    }

    override fun onRun() {
        quizStarted.value = true
        questionMode.value = QuestionMode.NOTHING
        super.onRun()
        setButtons()
    }

    override fun onNext() {
        if (step.value == steps.value.size - 1) return

        super.onNext()
        questionMode.value = when (val possibleQuestion = graphicalGraph.value.stepType) {
            is DepthFirstSearchStepType.SelectedVertex -> {
                QuestionMode.SELECT_VERTEX
            }

            is DepthFirstSearchStepType.SelectedNeighbour -> {
                graphicalGraph.value = graphicalGraph.value
                    .removeEdgeHighlight(possibleQuestion.vertex, possibleQuestion.neighbour)
                    .changeVertexHighlight(possibleQuestion.neighbour, Color.Transparent)
                QuestionMode.SELECT_NEIGHBOUR
            }

            is DepthFirstSearchStepType.NeighbourEvaluated -> {
                if (possibleQuestion.neighbourStatus == NeighbourStatus.VISIT) {
                    graphicalGraph.value = graphicalGraph.value
                        .changeEdge(
                            possibleQuestion.vertex,
                            possibleQuestion.neighbour,
                            selected = false,
                            color = Color.Black,
                        )
                }
                QuestionMode.SELECT_NEIGHBOUR_STATUS
            }

            is DepthFirstSearchStepType.VertexActive -> {
                tableEditable.value = true
                tableReachedContent.value = tableReachedContent.value.toMutableMap().apply {
                    this[possibleQuestion.vertex.name] = ""
                }
                tableParentContent.value = tableParentContent.value.toMutableMap().apply {
                    this[possibleQuestion.vertex.name] = "*"
                }
                QuestionMode.FILL_TABLE
            }

            is DepthFirstSearchStepType.Backtracking -> {
                tableEditable.value = true
                tableFinishedContent.value = tableFinishedContent.value.toMutableMap().apply {
                    this[possibleQuestion.vertex.name] = ""
                }
                QuestionMode.FILL_TABLE
            }

            else -> {
                QuestionMode.NOTHING
            }
        }
        setButtons()
    }

    override fun onBack() {
        questionMode.value = QuestionMode.NOTHING
        super.onBack()
        setButtons()
    }

    private fun setButtons() {
        nextEnabled.value =
            (step.value < steps.value.size - 1 && questionMode.value == QuestionMode.NOTHING) ||
            questionMode.value == QuestionMode.SHOW_ANSWER
        backEnabled.value = step.value > 0
    }

    override fun onTap(
        x: Double,
        y: Double,
    ) {
        if (!quizStarted.value) super.onTap(x, y)
    }

    override fun setGraphicalGraph() {
        super.setGraphicalGraph()

        tableReachedContent.value = currentGraph.value.vertices.associate {
            it.name to
                (it.reachedNumber?.toString() ?: "")
        }
        tableFinishedContent.value = currentGraph.value.vertices.associate {
            it.name to
                (it.finishedNumber?.toString() ?: "")
        }
        tableParentContent.value =
            currentGraph.value.vertices.associate { it.name to (it.parent?.name ?: "*") }
    }

    override fun onStepChange(newValue: String): Unit =
        throw IllegalStateException("Step change is not allowed in quiz mode.")

    fun onVertexAnswer(answer: DepthFirstSearchVertex) {
        val question = graphicalGraph.value.stepType as DepthFirstSearchStepType.SelectedVertex
        lastAnswer.value =
            if (answer.id == question.vertex.id) {
                Answer.Correct
            } else {
                Answer.Incorrect("A következő csúcs: ${question.vertex.name}.")
            }
        showAnswer()
    }

    fun onNeighbourAnswer(answer: DepthFirstSearchVertex) {
        val question = graphicalGraph.value.stepType as DepthFirstSearchStepType.SelectedNeighbour
        lastAnswer.value =
            if (answer.id == question.neighbour.id) {
                Answer.Correct
            } else {
                Answer.Incorrect("A következő szomszéd: ${question.neighbour.name}.")
            }
        setGraphicalGraph()
        showAnswer()
    }

    fun onNeighbourStatusAnswer(answer: NeighbourStatus) {
        val question =
            graphicalGraph.value.stepType as DepthFirstSearchStepType.NeighbourEvaluated
        lastAnswer.value =
            if (answer == question.neighbourStatus) {
                Answer.Correct
            } else {
                val correctAnswer =
                    if (question.neighbourStatus == NeighbourStatus.VISIT) {
                        "A ${question.neighbour.name} csúcsot még nem vizsgáltuk meg, ezért bejárjuk."
                    } else {
                        "A ${question.neighbour.name} csúcsot már megvizsgáltuk, ezért nem járjuk be újra."
                    }
                Answer.Incorrect(correctAnswer)
            }
        setGraphicalGraph()
        showAnswer()
    }

    fun onFillTableAnswer() {
        tableEditable.value = false

        val currentVertex = when (val question = graphicalGraph.value.stepType) {
            is DepthFirstSearchStepType.VertexActive -> question.vertex
            is DepthFirstSearchStepType.Backtracking -> question.vertex
            else -> throw IllegalStateException("Current step is not a table fill step.")
        }

        val vertices = currentGraph.value.vertices

        val correctReached = vertices.associate { it.name to (it.reachedNumber?.toString() ?: "") }
        val correctFinished = vertices.associate { it.name to (it.finishedNumber?.toString() ?: "") }
        val correctParent = vertices.associate { it.name to (it.parent?.name ?: "*") }

        for (vertex in vertices.filterNot { it.name == currentVertex.name }) {
            if (tableReachedContent.value[vertex.name] != correctReached[vertex.name] ||
                tableFinishedContent.value[vertex.name] != correctFinished[vertex.name] ||
                tableParentContent.value[vertex.name] != correctParent[vertex.name]
            ) {
                lastAnswer.value =
                    Answer.Incorrect(
                        "Jelenleg a ${currentVertex.name} csúcsot vizsgáljuk, csak a megfelelő oszlopot kell kitölteni.",
                    )
                showAnswer()
                return
            }
        }
        if (tableReachedContent.value[currentVertex.name] != correctReached[currentVertex.name]) {
            lastAnswer.value =
                Answer.Incorrect(
                    "A ${currentVertex.name} csúcs mélységi száma ${currentVertex.reachedNumber ?: "még ismeretlen"}",
                )
            showAnswer()
            return
        }
        if (tableFinishedContent.value[currentVertex.name] != correctFinished[currentVertex.name]) {
            lastAnswer.value =
                Answer.Incorrect(
                    "A ${currentVertex.name} csúcs befejezési száma ${currentVertex.finishedNumber ?: "még ismeretlen"}",
                )
            showAnswer()
            return
        }
        if (tableParentContent.value[currentVertex.name] != correctParent[currentVertex.name]) {
            lastAnswer.value =
                Answer.Incorrect(
                    "A ${currentVertex.name} csúcs előzője ${currentVertex.parent?.name ?: "*"}",
                )
            showAnswer()
            return
        }
        lastAnswer.value = Answer.Correct
        showAnswer()
    }

    private fun showAnswer() {
        questionMode.value = QuestionMode.SHOW_ANSWER
        setButtons()
    }

    enum class QuestionMode {
        NOTHING,
        SHOW_ANSWER,
        SELECT_VERTEX,
        SELECT_NEIGHBOUR,
        SELECT_NEIGHBOUR_STATUS,
        FILL_TABLE,
    }
}
