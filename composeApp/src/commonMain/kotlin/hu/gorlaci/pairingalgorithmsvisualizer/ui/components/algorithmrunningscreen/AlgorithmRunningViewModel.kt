package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.SkipPoint
import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph

abstract class AlgorithmRunningViewModel(
    graphStorage: GraphStorage,
) : ViewModel() {

    protected open val initString = "Jelöld ki a kiinduló párosítást!"

    open val graphList = graphStorage.getAllGraphs()

    protected val selectedGraphIndex = mutableStateOf(0)

    open val selectedGraph by lazy {
        derivedStateOf {
            graphList[selectedGraphIndex.value]
        }
    }

    abstract val steps: State<List<GraphicalGraph>>
    val step = mutableStateOf(0)

    val maxStep = derivedStateOf {
        steps.value.size
    }

    open val graphicalGraph by lazy {
        mutableStateOf(
            selectedGraph.value.toGraphicalGraph(StepType.Nothing(initString)),
        )
    }

    open val nextEnabled = derivedStateOf {
        step.value < maxStep.value - 1
    }

    open val backEnabled = derivedStateOf {
        step.value > 0
    }

    val runEnabled = mutableStateOf(true)

    open val skipForwardEnabled = derivedStateOf {
        nextEnabled.value
    }

    open val skipBackwardEnabled = derivedStateOf {
        backEnabled.value
    }

    private fun setGraphicalGraph() {
        graphicalGraph.value = steps.value[step.value]
    }

    open fun onNext() {
        step.value++
        setGraphicalGraph()
    }

    open fun onBack() {
        step.value--
        setGraphicalGraph()
    }

    open fun onStepChange(newValue: String) {
        val newStep = try {
            newValue.toInt() - 1
        } catch (_: NumberFormatException) {
            return
        }
        if (newStep < 0) {
            return
        }
        if (newStep >= maxStep.value) {
            return
        }
        step.value = newStep
        setGraphicalGraph()
    }

    fun onSkipForward() {
        for (i in step.value + 1 until maxStep.value) {
            if (steps.value[i].stepType is SkipPoint) {
                step.value = i
                setGraphicalGraph()
                return
            }
        }
        step.value = maxStep.value - 1
    }

    fun onSkipBackward() {
        for (i in step.value - 1 downTo 0) {
            if (steps.value[i].stepType is SkipPoint) {
                step.value = i
                setGraphicalGraph()
                return
            }
        }
        step.value = 0
    }

    abstract fun onGraphSelected(index: Int)

    abstract fun onRun()

    var selectedVertex: Vertex? = null

    open fun onTap(
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
            if (graph.getPair(clickedVertex) == selectedVertexNotNull) {
                graph.unPairVertices(clickedVertex, selectedVertexNotNull)
                selectedVertex = null
                graphicalGraph.value = graph.toGraphicalGraph(StepType.Nothing(initString))
                return
            }
            if (
                selectedVertexNotNull in graph.getNeighbours(clickedVertex) &&
                graph.getPair(clickedVertex) == null &&
                graph.getPair(selectedVertexNotNull) == null
            ) {
                graph.pairVertices(clickedVertex, selectedVertexNotNull)
                selectedVertex = null
                graphicalGraph.value = graph.toGraphicalGraph(StepType.Nothing(initString))
                return
            }
        }
    }
}
