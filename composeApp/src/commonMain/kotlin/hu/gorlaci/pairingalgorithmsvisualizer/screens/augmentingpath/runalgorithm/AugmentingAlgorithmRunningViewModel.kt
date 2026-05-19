package hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.runalgorithm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.AugmentingPathGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.AugmentingPathVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphDisplayMode
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph

class AugmentingAlgorithmRunningViewModel(
    val graphStorage: GraphStorage,
) : ViewModel() {

    companion object {
        const val INIT_STRING = "Jelöld ki a kiinduló párosítást!"
    }

    val graphList = graphStorage.getAllAugmentingPathGraphs()
    private var selectedGraphIndex = 0

    val selectedGraph: MutableState<AugmentingPathGraph> =
        mutableStateOf(graphList[selectedGraphIndex])

    private val steps = mutableStateOf(
        listOf(
            selectedGraph.value.toGraphicalGraph(StepType.Nothing(INIT_STRING)) to
                AugmentingPathGraph(),
        ),
    )
    val step = mutableStateOf(0)

    val maxSteps = derivedStateOf {
        steps.value.size
    }

    val graphicalGraph = mutableStateOf(steps.value[0].first)
    val tree = mutableStateOf(
        GraphicalGraph(
            listOf(),
            listOf(),
            StepType.Nothing(),
        ),
    )
    val class1Ids = mutableStateOf(emptyList<String>())
    val class2Ids = mutableStateOf(emptyList<String>())

    val nextEnabled = mutableStateOf(false)
    val backEnabled = mutableStateOf(false)
    val runEnabled = mutableStateOf(true)

    fun onNext() {
        if (step.value < steps.value.size - 1) {
            step.value++

            setCurrentGraph()
        }
        setButtons()
    }

    fun onBack() {
        if (step.value > 0) {
            step.value--
            setCurrentGraph()
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

    val skipForwardEnabled = derivedStateOf {
        val stepsRemaining = steps.value.subList(step.value + 1, steps.value.size).map { it.first }
        stepsRemaining.any { it.stepType is StepType.AugmentingPathFound }
    }

    val skipBackwardEnabled = derivedStateOf {
        step.value > 0
    }

    fun onSkipForward() {
        for (i in step.value + 1 until steps.value.size) {
            if (steps.value[i].first.stepType is StepType.AugmentingPathFound) {
                step.value = i
                setCurrentGraph()
                setButtons()
                return
            }
        }
    }

    fun onSkipBackward() {
        for (i in step.value - 1 downTo 0) {
            if (steps.value[i].first.stepType is StepType.AugmentingPathFound) {
                step.value = i
                setCurrentGraph()
                setButtons()
                return
            }
        }
        step.value = 0
    }

    private fun setCurrentGraph() {
        graphicalGraph.value = steps.value[step.value].first
        tree.value = steps.value[step.value].second.toGraphicalGraph()
    }

    private fun setButtons() {
        nextEnabled.value = step.value < steps.value.size - 1
        backEnabled.value = step.value > 0
    }

    fun onGraphSelected(index: Int) {
        selectedGraphIndex = index
        selectedGraph.value = graphList[selectedGraphIndex]

        steps.value =
            listOf(
                selectedGraph.value.toGraphicalGraph(StepType.Nothing(INIT_STRING)) to
                    AugmentingPathGraph(),
            )
        step.value = 0
        getClasses()
        setCurrentGraph()
        setButtons()
        runEnabled.value = true
    }

    private fun getClasses() {
        val graph = selectedGraph.value
        class1Ids.value = graph.class1.map { it.name }
        class2Ids.value = graph.class2.map { it.name }
    }

    fun onRun() {
        val graph = graphList[selectedGraphIndex]

        graph.runAlgorithm()

        steps.value = graph.steps

        step.value = 0
        getClasses()
        setCurrentGraph()
        setButtons()
        runEnabled.value = false
    }

    val graphDisplayMode = mutableStateOf(GraphDisplayMode.BOTH)

    fun changeDisplayMode(newValue: GraphDisplayMode) {
        graphDisplayMode.value = newValue
    }

    var selectedVertex: AugmentingPathVertex? = null

    fun onTap(
        x: Double,
        y: Double,
    ) {
        val graph = selectedGraph.value
        val clickedVertex = graph.getVertexByCoordinates(x, y) ?: return
        if (selectedVertex == null) {
            selectedVertex = clickedVertex
            graphicalGraph.value = graphicalGraph.value.changeInnerColor(
                clickedVertex,
                LIGHT_ORANGE,
            )
            return
        }
        if (selectedVertex == clickedVertex) {
            selectedVertex = null
            graphicalGraph.value = graphicalGraph.value.changeInnerColor(
                clickedVertex,
                Color.White,
            )
            return
        }
        selectedVertex?.let { selectedVertexNotNull ->
            if (clickedVertex.pair == selectedVertexNotNull) {
                clickedVertex.pair = null
                selectedVertexNotNull.pair = null
                selectedVertex = null
            } else if (clickedVertex.pair == null && selectedVertexNotNull.pair == null) {
                selectedVertexNotNull.pair = clickedVertex
                clickedVertex.pair = selectedVertexNotNull
                selectedVertex = null
            }
            graphicalGraph.value = graph.toGraphicalGraph(StepType.Nothing(INIT_STRING))
        }
    }
}
