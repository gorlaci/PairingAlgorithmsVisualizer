package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.BreadthFirstSearchVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.quiz.BreadthFirstSearchStepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.quiz.NeighbourStatus
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.Answer
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel

class BreadthFirstSearchQuizScreenViewModel(
    graphStorage: GraphStorage,
) : BreadthFirstSearchAlgorithmRunningViewModel(graphStorage) {
    override val nextEnabled = mutableStateOf(false)
    override val backEnabled = mutableStateOf(false)

    val quizStarted = mutableStateOf(false)
    val questionMode = mutableStateOf(QuestionMode.NOTHING)
    val lastAnswer: MutableState<Answer> = mutableStateOf(Answer.Correct)

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
        questionMode.value = QuestionMode.NOTHING

        val possibleQuestion = graphicalGraph.value.stepType

        when (possibleQuestion) {
            is BreadthFirstSearchStepType.SelectedVertex -> {
                questionMode.value = QuestionMode.SELECT_VERTEX
            }

            is BreadthFirstSearchStepType.SelectedNeighbour -> {
                questionMode.value = QuestionMode.SELECT_NEIGHBOUR_STATUS
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
        showAnswer()
    }

    fun onNeighbourStatusAnswer(answer: NeighbourStatus) {
        val question =
            graphicalGraph.value.stepType as BreadthFirstSearchStepType.SelectedNeighbour
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
        SELECT_NEIGHBOUR_STATUS,
    }
}
