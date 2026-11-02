package hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds.runalgorithm

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsEdge
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.quiz.EdmondsStepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.toEdmondsGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.PairingAlgorithmRunningViewModel
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class EdmondsAlgorithmRunningScreenViewModel(
    graphStorage: GraphStorage,
    protected val composableCoroutineContext: CoroutineContext,
) : PairingAlgorithmRunningViewModel<
    EdmondsGraph,
    EdmondsVertex,
    EdmondsEdge,
    >() {
    override val graphList = derivedStateOf {
        graphStorage.getAllGraphs().value.map { it.toEdmondsGraph() }
    }

    val currentGraph = mutableStateOf(graphList.value[selectedGraphIndex.value])

    protected val edmondsSteps = mutableStateOf(
        listOf<Pair<EdmondsGraph, EdmondsStepType>>(
            currentGraph.value to EdmondsStepType.Nothing(initString),
        ),
    )
    override val steps = derivedStateOf {
        edmondsSteps.value.map { it.first.toGraphicalGraph(it.second) }
    }

    override fun onNext() {
        if (step.value < edmondsSteps.value.size - 1) {
            step.value++

            setCurrentGraph()

            if (graphicalGraph.value.stepType is EdmondsStepType.BlossomInAnimation ||
                graphicalGraph.value.stepType is EdmondsStepType.BlossomOutAnimation
            ) {
                startBlossomAnimation()
            }
        }
    }

    override fun onBack() {
        if (step.value > 0) {
            step.value--
            setCurrentGraph()

            if (graphicalGraph.value.stepType is EdmondsStepType.BlossomInAnimation) {
                // skip blossom animation on back
                step.value--
                setCurrentGraph()
            }
        }
    }

    override fun onStepChange(newValue: String) {
        super.onStepChange(newValue)
        setCurrentGraph()
    }

    protected fun setCurrentGraph() {
        currentGraph.value = edmondsSteps.value[step.value].first
        graphicalGraph.value =
            edmondsSteps.value[step.value].first.toGraphicalGraph(edmondsSteps.value[step.value].second)
    }

    override fun onGraphSelected(index: Int) {
        selectedGraphIndex.value = index
        currentGraph.value = graphList.value[selectedGraphIndex.value]

        currentGraph.value.resetAlgorithm()

        edmondsSteps.value = listOf(currentGraph.value to EdmondsStepType.Nothing(initString))

        graphicalGraph.value =
            currentGraph.value.toGraphicalGraph(EdmondsStepType.Nothing(initString))

        step.value = 0
        inSetup.value = true

        selectedVertex = null
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
                                        .animateBlossomVertices(
                                            stepType.blossomVertices,
                                            currentGraph.value,
                                            value,
                                        )
                            }

                            is EdmondsStepType.BlossomOutAnimation -> {
                                graphicalGraph.value =
                                    graphicalGraph.value
                                        .animateBlossomVertices(
                                            stepType.blossomVertices,
                                            currentGraph.value,
                                            1f - value,
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

    override fun onRun() {
        val graph = graphList.value[selectedGraphIndex.value]

        graph.runAlgorithm()
        edmondsSteps.value = graph.steps

        step.value = 0
        inSetup.value = false

        selectedVertex = null
    }
}
