package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.BreadthFirstSearchVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.quiz.BreadthFirstSearchStepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.quiz.NeighbourStatus
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.Answer
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.ui.GRAY
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.RED

class BreadthFirstSearchQuizScreenViewModel(
    graphStorage: GraphStorage,
) : BreadthFirstSearchAlgorithmRunningViewModel(graphStorage) {
    override val nextEnabled = mutableStateOf(false)
    override val backEnabled = mutableStateOf(false)

    val quizStarted = mutableStateOf(false)
    val questionMode = mutableStateOf(QuestionMode.NOTHING)
    val lastAnswer: MutableState<Answer> = mutableStateOf(Answer.Correct)

    val tableEditable = mutableStateOf(false)
    val tableDistanceContent = mutableStateOf(currentGraph.value.vertices.map { it.name }.associateWith { "∞" })
    val tableParentContent = mutableStateOf(currentGraph.value.vertices.map { it.name }.associateWith { "*" })

    override val tree = mutableStateOf(Graph.getEmpty().toGraphicalGraph())

    override fun setGraphicalGraph() {
        super.setGraphicalGraph()
        tree.value = _steps.value[step.value].third.toGraphicalGraph()
    }

    fun onTableDistanceChanged(
        vertex: BreadthFirstSearchVertex,
        newValue: String,
    ) {
        tableDistanceContent.value = tableDistanceContent.value.toMutableMap().apply {
            this[vertex.name] = newValue
        }
    }

    fun onTableParentChanged(
        vertex: BreadthFirstSearchVertex,
        newValue: String,
    ) {
        tableParentContent.value = tableParentContent.value.toMutableMap().apply {
            this[vertex.name] = newValue
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
        if (step.value == steps.value.size - 1) {
            return
        }

        super.onNext()

        tableDistanceContent.value =
            currentGraph.value.vertices.associate { it.name to (it.distance?.toString() ?: "∞") }
        tableParentContent.value =
            currentGraph.value.vertices.associate { it.name to (it.parent?.name ?: "*") }

        questionMode.value = QuestionMode.NOTHING

        when (val possibleQuestion = graphicalGraph.value.stepType) {
            is BreadthFirstSearchStepType.SelectedVertex -> {
                questionMode.value = QuestionMode.SELECT_VERTEX
                graphicalGraph.value =
                    graphicalGraph.value.changeVertexHighlight(
                        possibleQuestion.vertex,
                        GRAY,
                    )
                tree.value = tree.value.changeVertexHighlight(possibleQuestion.vertex, GRAY)
            }

            is BreadthFirstSearchStepType.NeighbourEvaluated -> {
                questionMode.value = QuestionMode.SELECT_NEIGHBOUR_STATUS
                if (possibleQuestion.neighbourStatus == NeighbourStatus.ADDED) {
                    tree.value = tree.value
                        .removeVertex(possibleQuestion.neighbour)
                }
            }

            is BreadthFirstSearchStepType.SelectedNeighbour -> {
                questionMode.value = QuestionMode.SELECT_NEIGHBOUR
                graphicalGraph.value = graphicalGraph.value
                    .changeVertexHighlight(possibleQuestion.neighbour, Color.Transparent)
                    .removeEdgeHighlight(possibleQuestion.vertex, possibleQuestion.neighbour)
                tree.value = tree.value
                    .changeVertexHighlight(possibleQuestion.neighbour, Color.Transparent)
                    .removeEdgeHighlight(possibleQuestion.vertex, possibleQuestion.neighbour)
            }

            is BreadthFirstSearchStepType.FillTable -> {
                questionMode.value = QuestionMode.FILL_TABLE
                tableEditable.value = true

                tableDistanceContent.value =
                    tableDistanceContent.value.toMutableMap().apply {
                        this[possibleQuestion.neighbour.name] = "∞"
                    }
                tableParentContent.value =
                    tableParentContent.value.toMutableMap().apply {
                        this[possibleQuestion.neighbour.name] = "*"
                    }
            }

            else -> Unit
        }
        setButtons()
    }

    override fun onBack() {
        questionMode.value = QuestionMode.NOTHING
        super.onBack()
        setButtons()
    }

    override fun onStepChange(newValue: String): Unit =
        throw UnsupportedOperationException("Step change is not supported in quiz mode.")

    private fun setButtons() {
        nextEnabled.value =
            (step.value < steps.value.size - 1 && questionMode.value == QuestionMode.NOTHING) ||
            (questionMode.value == QuestionMode.SHOW_ANSWER)
        backEnabled.value = step.value > 0
    }

    override fun onTap(
        x: Double,
        y: Double,
    ) {
        if (!quizStarted.value) {
            super.onTap(x, y)
        }
    }

    fun onVertexAnswer(answer: BreadthFirstSearchVertex) {
        val question = graphicalGraph.value.stepType as BreadthFirstSearchStepType.SelectedVertex
        lastAnswer.value =
            if (answer.id == question.vertex.id) {
                Answer.Correct
            } else {
                Answer.Incorrect("A következő csúcs: ${question.vertex.name}.")
            }
        graphicalGraph.value = graphicalGraph.value.changeVertexHighlight(question.vertex, LIGHT_ORANGE)
        showAnswer()
    }

    fun onNeighbourAnswer(answer: BreadthFirstSearchVertex) {
        val question =
            graphicalGraph.value.stepType as BreadthFirstSearchStepType.SelectedNeighbour
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
            graphicalGraph.value.stepType as BreadthFirstSearchStepType.NeighbourEvaluated
        lastAnswer.value =
            if (answer == question.neighbourStatus) {
                Answer.Correct
            } else {
                val explanation =
                    if (question.neighbourStatus == NeighbourStatus.ADDED) {
                        "A ${question.neighbour.name} csúcsot még nem vizsgáltuk meg, ezért bekerül a listába."
                    } else {
                        "A ${question.neighbour.name} csúcsot már megvizsgáltuk, vagy szerepel a listában."
                    }
                Answer.Incorrect(explanation)
            }
        setGraphicalGraph()
        graphicalGraph.value = graphicalGraph.value.apply {
            addEdge(
                question.vertex,
                question.neighbour,
                selected = true,
                color = RED,
            )
        }
        showAnswer()
    }

    fun onFillTableAnswer() {
        tableEditable.value = false
        val question = graphicalGraph.value.stepType as BreadthFirstSearchStepType.FillTable

        val correctDistances = currentGraph.value.vertices.associate {
            it.name to (it.distance?.toString() ?: "∞")
        }
        val correctParents = currentGraph.value.vertices.associate {
            it.name to (it.parent?.name ?: "*")
        }

        for (vertex in currentGraph.value.vertices.filter { it.id != question.neighbour.id }) {
            if (tableDistanceContent.value[vertex.name] != correctDistances[vertex.name]) {
                lastAnswer.value = Answer.Incorrect(
                    "Jelenleg a ${question.neighbour.name} csúcsot vizsgáljuk, csak a megfelelő oszlopot kell kitölteni.",
                )
                showAnswer()
                return
            }
            if (tableParentContent.value[vertex.name] != correctParents[vertex.name]) {
                lastAnswer.value = Answer.Incorrect(
                    "Jelenleg a ${question.neighbour.name} csúcsot vizsgáljuk, csak a megfelelő oszlopot kell kitölteni.",
                )
                showAnswer()
                return
            }
        }
        if (tableDistanceContent.value[question.neighbour.name] !=
            correctDistances[question.neighbour.name]
        ) {
            lastAnswer.value =
                Answer.Incorrect(
                    "A ${question.neighbour.name} csúcs távolsága ${question.neighbour.distance}.",
                )
            showAnswer()
            return
        }
        if (tableParentContent.value[question.neighbour.name] != correctParents[question.neighbour.name]) {
            lastAnswer.value =
                Answer.Incorrect(
                    "A ${question.neighbour.name} csúcs előzője ${question.neighbour.parent?.name ?: "*"}.",
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
