package hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.quiz

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.run_algorithm.EdmondsAlgorithmRunningScreenViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsEdge
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.quiz.EdmondsAnswer
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.quiz.EdmondsEdgeType
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.quiz.EdmondsStepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.BLUE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.PINK
import hu.gorlaci.pairingalgorithmsvisualizer.util.containsSameEdges
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class QuizScreenViewmodel(
    graphStorage: GraphStorage,
    composableCoroutineContext: CoroutineContext,
) : EdmondsAlgorithmRunningScreenViewModel(graphStorage, composableCoroutineContext) {
    val quizStarted = mutableStateOf(false)

    val questionMode = mutableStateOf(QuestionMode.NOTHING)
    val lastAnswer = mutableStateOf<EdmondsAnswer>(EdmondsAnswer.Correct)

    val questionFrequency = mutableStateOf(1f)

    override fun onNext() {
        if (step < steps.size - 1) {
            step++

            setCurrentGraph()

            questionMode.value = QuestionMode.NOTHING

            val possibleQuestion = graphicalGraph.value.stepType

            if (possibleQuestion is EdmondsStepType.BlossomInAnimation || possibleQuestion is EdmondsStepType.BlossomOutAnimation) {
                startBlossomAnimation()
                setButtons()
                return
            }

            val random = Random.nextFloat()

            if (random < questionFrequency.value) {
                when (possibleQuestion) {
                    is EdmondsStepType.SelectedEdge -> {
                        questionMode.value = QuestionMode.EDGE_TYPE
                    }

                    is EdmondsStepType.MarkAugmentingPath -> {
                        questionMode.value = QuestionMode.MARK_AUGMENTING_PATH
                        clearMarkedVertices()
                        graphicalGraph.value =
                            graphicalGraph.value
                                .removeAllEdgeHighlights()
                                .addHighlight(possibleQuestion.currentEdge, ORANGE)
                    }

                    is EdmondsStepType.MarkBlossom -> {
                        questionMode.value = QuestionMode.MARK_BLOSSOM
                        clearMarkedVertices()
                        graphicalGraph.value =
                            graphicalGraph.value
                                .removeAllEdgeHighlights()
                                .addHighlight(possibleQuestion.currentEdge, ORANGE)
                    }

                    else -> {}
                }
            }
        }
        setButtons()
    }

    override fun onBack() {
        questionMode.value = QuestionMode.NOTHING

        super.onBack()
    }

    override fun onRun() {
        questionMode.value = QuestionMode.NOTHING
        quizStarted.value = true

        super.onRun()
    }

    override fun setButtons() {
        nextEnabled.value =
            step < steps.size - 1 && questionMode.value == QuestionMode.NOTHING || questionMode.value == QuestionMode.SHOW_ANSWER
        backEnabled.value = step > 0
    }

    override fun onGraphSelected(index: Int) {
        quizStarted.value = false
        questionMode.value = QuestionMode.NOTHING

        super.onGraphSelected(index)
    }

    fun onEdgeTypeAnswer(answer: EdmondsEdgeType) {
        val question = graphicalGraph.value.stepType as EdmondsStepType.SelectedEdge

        lastAnswer.value =
            if (answer == question.edgeType) {
                EdmondsAnswer.Correct
            } else {
                EdmondsAnswer.Incorrect("Ez egy ${question.edgeType.toHungarian()} él.")
            }

        showAnswer()
    }

    val markedVertices = mutableStateOf(listOf<EdmondsVertex>())
    val confirmationDisplayed = mutableStateOf(false)

    private fun clearMarkedVertices() {
        markedVertices.value = listOf()
    }

    fun onClick(
        x: Double,
        y: Double,
    ) {
        val possibleQuestion = graphicalGraph.value.stepType

        if (possibleQuestion is EdmondsStepType.MarkAugmentingPath ||
            possibleQuestion is EdmondsStepType.MarkBlossom
        ) {
            val graph = currentGraph.value

            val clickedVertex = graph.getVertexByCoordinates(x, y)
            if (clickedVertex != null) {
                if (markedVertices.value.lastOrNull() == clickedVertex) {
                    markedVertices.value = markedVertices.value.dropLast(1)
                    graphicalGraph.value =
                        graphicalGraph.value
                            .changeInnerColor(clickedVertex, Color.White)
                } else {
                    markedVertices.value += clickedVertex
                    graphicalGraph.value =
                        graphicalGraph.value
                            .changeInnerColor(clickedVertex, ORANGE)
                }
            }
        }
    }

    private fun getMarkedEdges(): Set<EdmondsEdge> {
        val markedEdges = mutableSetOf<EdmondsEdge>()
        for (i in 0..markedVertices.value.size - 2) {
            val aId = markedVertices.value[i].id
            val bId = markedVertices.value[i + 1].id
            val edge =
                currentGraph.value.edges.find { edge ->
                    (edge.fromVertex.id == aId && edge.toVertex.id == bId) ||
                            (edge.fromVertex.id == bId && edge.toVertex.id == aId)
                }
            if (edge != null) {
                markedEdges.add(edge)
            }
        }
        return markedEdges
    }

    fun onSubmit() {
        val possibleQuestion = graphicalGraph.value.stepType

        when (possibleQuestion) {
            is EdmondsStepType.MarkAugmentingPath -> {
                onAugmentingPathSubmitted()
            }

            is EdmondsStepType.MarkBlossom -> {
                onBlossomSubmitted()
            }

            else -> {}
        }
        confirmationDisplayed.value = false
    }

    fun onAugmentingPathSubmitted() {
        val question = graphicalGraph.value.stepType as EdmondsStepType.MarkAugmentingPath

        lastAnswer.value =
            if (containsSameEdges(getMarkedEdges(), question.pathEdges)) {
                EdmondsAnswer.Correct
            } else {
                EdmondsAnswer.Incorrect(
                    "A javító út a következő élekből áll: ${
                        question.pathEdges.joinToString {
                            "(${it.fromVertex.id}, ${it.toVertex.id})"
                        }
                    }",
                )
            }
        showAnswer()
    }

    fun onBlossomSubmitted() {
        val question = graphicalGraph.value.stepType as EdmondsStepType.MarkBlossom

        lastAnswer.value =
            if (containsSameEdges(getMarkedEdges(), question.blossomEdges)) {
                EdmondsAnswer.Correct
            } else {
                EdmondsAnswer.Incorrect(
                    "A kelyhet a következő élek alkotják: ${
                        question.blossomEdges.joinToString {
                            "(${it.fromVertex.id}, ${it.toVertex.id})"
                        }
                    }",
                )
            }
        showAnswer()
    }

    fun displayConfirmation() {
        val augmentingPath = questionMode.value == QuestionMode.MARK_AUGMENTING_PATH

        var newGraphicalGraph = graphicalGraph.value
        for (edge in getMarkedEdges()) {
            newGraphicalGraph = newGraphicalGraph.addHighlight(edge, if (augmentingPath) BLUE else PINK)
        }
        graphicalGraph.value = newGraphicalGraph

        confirmationDisplayed.value = true
    }

    fun onContinueSelection() {
        graphicalGraph.value =
            graphicalGraph.value
                .removeAllEdgeHighlights()
                .addHighlight(
                    edge =
                        (graphicalGraph.value.stepType as? EdmondsStepType.MarkAugmentingPath)?.currentEdge
                            ?: (graphicalGraph.value.stepType as? EdmondsStepType.MarkBlossom)?.currentEdge
                            ?: return,
                    color = ORANGE,
                )

        confirmationDisplayed.value = false
    }

    private fun showAnswer() {
        (graphicalGraph.value.stepType as? EdmondsStepType.SelectedEdge)?.let {
            if (it.edgeType == EdmondsEdgeType.OUTER_OUTER) {
                onNext()
            }
        }
        questionMode.value = QuestionMode.SHOW_ANSWER
        setButtons()
    }

    fun onQuestionFrequencyChange(newFrequency: Float) {
        questionFrequency.value = newFrequency
    }
}

enum class QuestionMode {
    NOTHING,
    SHOW_ANSWER,
    EDGE_TYPE,
    MARK_AUGMENTING_PATH,
    MARK_BLOSSOM,
}
