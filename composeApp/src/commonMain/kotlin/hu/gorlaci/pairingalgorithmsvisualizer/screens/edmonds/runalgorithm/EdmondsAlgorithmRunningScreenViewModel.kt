package hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds.runalgorithm

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.quiz.EdmondsStepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningViewModel
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class EdmondsAlgorithmRunningScreenViewModel(
    graphStorage: GraphStorage,
    protected val composableCoroutineContext: CoroutineContext,
) : AlgorithmRunningViewModel(graphStorage) {
    override val graphList = graphStorage.getAllEdmondsGraphs()

    val currentGraph = mutableStateOf(graphList[selectedGraphIndex.value])

    @Suppress("ktlint:standard:backing-property-naming")
    protected val _steps = mutableStateOf(
        listOf<Pair<EdmondsGraph, EdmondsStepType>>(
            currentGraph.value to EdmondsStepType.Nothing(initString),
        ),
    )
    override val steps = derivedStateOf {
        _steps.value.map { it.first.toGraphicalGraph(it.second) }
    }

    override fun onNext() {
        if (step.value < _steps.value.size - 1) {
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
        currentGraph.value = _steps.value[step.value].first
        graphicalGraph.value =
            _steps.value[step.value].first.toGraphicalGraph(_steps.value[step.value].second)
    }

    override fun onGraphSelected(index: Int) {
        selectedGraphIndex.value = index
        currentGraph.value = graphList[selectedGraphIndex.value]

        _steps.value = listOf(currentGraph.value to EdmondsStepType.Nothing(initString))

        graphicalGraph.value =
            currentGraph.value.toGraphicalGraph(EdmondsStepType.Nothing(initString))

        step.value = 0
        runEnabled.value = true
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
        val graph = graphList[selectedGraphIndex.value]

        graph.runEdmondsAlgorithm()
        _steps.value = graph.steps

        step.value = 0
        runEnabled.value = false
    }
}
