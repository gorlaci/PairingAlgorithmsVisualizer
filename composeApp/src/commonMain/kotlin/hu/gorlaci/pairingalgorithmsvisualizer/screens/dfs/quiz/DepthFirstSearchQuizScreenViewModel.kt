package hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs.quiz

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
        questionMode.value = when (graphicalGraph.value.stepType) {
            is DepthFirstSearchStepType.SelectedVertex -> QuestionMode.SELECT_VERTEX
            is DepthFirstSearchStepType.SelectedNeighbour -> QuestionMode.SELECT_NEIGHBOUR_STATUS
            else -> QuestionMode.NOTHING
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

    override fun onTap(x: Double, y: Double) {
        if (!quizStarted.value) super.onTap(x, y)
    }

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

    fun onNeighbourStatusAnswer(answer: NeighbourStatus) {
        val question =
            graphicalGraph.value.stepType as DepthFirstSearchStepType.SelectedNeighbour
        lastAnswer.value =
            if (answer == question.neighbourStatus) {
                Answer.Correct
            } else {
                val explanation =
                    if (question.neighbourStatus == NeighbourStatus.VISIT) {
                        "A ${question.neighbour.name} csúcsot még nem vizsgáltuk meg, ezért bejárjuk."
                    } else {
                        "A ${question.neighbour.name} csúcsot már megvizsgáltuk, ezért nem járjuk be újra."
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
