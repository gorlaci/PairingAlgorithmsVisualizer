package hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.run_algorithm

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.quiz.EdmondsStepType
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

open class EdmondsAlgorithmRunningScreenViewModel(
    protected val graphStorage: GraphStorage,
    protected val composableCoroutineContext: CoroutineContext,
) : ViewModel() {
    val graphList = graphStorage.getAllEdmondsGraphs()
    protected var selectedGraphIndex = 0
    val currentGraph = mutableStateOf(graphList[selectedGraphIndex])

    protected val steps = mutableStateOf(
        listOf<Pair<EdmondsGraph, EdmondsStepType>>(currentGraph.value to EdmondsStepType.Nothing())
    )
    val step = mutableStateOf(0)

    val maxStep = derivedStateOf {
        steps.value.size
    }

    val graphicalGraph = mutableStateOf(steps.value[0].first.toGraphicalGraph())

    val nextEnabled = mutableStateOf(false)
    val backEnabled = mutableStateOf(false)
    val runEnabled = mutableStateOf(true)

    open fun onNext() {
        if (step.value < steps.value.size - 1) {
            step.value++

            setCurrentGraph()

            if (graphicalGraph.value.stepType is EdmondsStepType.BlossomInAnimation ||
                graphicalGraph.value.stepType is EdmondsStepType.BlossomOutAnimation
            ) {
                startBlossomAnimation()
            }
        }
        setButtons()
    }

    open fun onBack() {
        if (step.value > 0) {
            step.value--
            setCurrentGraph()

            if (graphicalGraph.value.stepType is EdmondsStepType.BlossomInAnimation) {
                // skip blossom animation on back
                step.value--
                setCurrentGraph()
            }
        }
        setButtons()
    }

    fun onStepChange(newValue: String) {
        val int = try {
            newValue.toInt() - 1
        } catch (_: NumberFormatException) {
            return
        }

        if (int < 0) {
            return
        }
        if (int >= steps.value.size) {
            return
        }
        step.value = int
        setCurrentGraph()
        setButtons()
    }

    protected fun setCurrentGraph() {
        currentGraph.value = steps.value[step.value].first
        graphicalGraph.value = steps.value[step.value].first.toGraphicalGraph(steps.value[step.value].second)
    }

    open fun setButtons() {
        nextEnabled.value = step.value < steps.value.size - 1
        backEnabled.value = step.value > 0
    }

    open fun onGraphSelected(index: Int) {
        selectedGraphIndex = index
        currentGraph.value = graphList[selectedGraphIndex]

        steps.value = listOf(currentGraph.value to EdmondsStepType.Nothing())

        graphicalGraph.value = currentGraph.value.toGraphicalGraph()
        step.value = 0
        runEnabled.value = true
        setButtons()
    }

    val blossomAnimationProgress = Animatable(0f)

    protected fun startBlossomAnimation() {
        viewModelScope.launch {
            withContext(composableCoroutineContext) {
                blossomAnimationProgress.snapTo(0f)
                blossomAnimationProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 800),
                    block = {
                        val stepType = graphicalGraph.value.stepType
                        when (stepType) {
                            is EdmondsStepType.BlossomInAnimation -> {
                                graphicalGraph.value =
                                    graphicalGraph.value
                                        .animateBlossomVertices(stepType.blossomVertices, currentGraph.value, value)
                            }

                            is EdmondsStepType.BlossomOutAnimation -> {
                                graphicalGraph.value =
                                    graphicalGraph.value
                                        .animateBlossomVertices(
                                            stepType.blossomVertices,
                                            currentGraph.value,
                                            1f - value
                                        )
                            }

                            else -> {}
                        }
                    },
                )
                onNext()
            }
        }
    }

    open fun onRun() {
        val graph = graphList[selectedGraphIndex]

        graph.runEdmondsAlgorithm()
        steps.value = graph.steps

        step.value = 0
        setButtons()
        runEnabled.value = false
    }
}
