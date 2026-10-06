package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.*
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.SkipPoint
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph

abstract class AlgorithmRunningViewModel<
    GraphType : Graph<VertexType, EdgeType>,
    VertexType : Vertex,
    EdgeType : Edge<VertexType>,
    >(
    graphStorage: GraphStorage,
) : ViewModel() {

    protected open val initString = "Jelöld ki a kiinduló párosítást!"

    abstract val graphList: List<GraphType>

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

    val inSetup = mutableStateOf(true)

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

    protected open var selectedVertex: VertexType? = null

    open fun onTap(
        x: Double,
        y: Double,
    ) {
        if (!inSetup.value) return

        val graph = selectedGraph.value
        val clickedVertex = graph.getVertexByCoordinates(x, y) ?: return

        selectedVertex?.let {
            graphicalGraph.value = graphicalGraph.value.changeInnerColor(
                it,
                Color.White,
            )
        }

        if (clickedVertex != selectedVertex) {
            selectedVertex = clickedVertex
            graphicalGraph.value = graphicalGraph.value.changeInnerColor(
                clickedVertex,
                LIGHT_ORANGE,
            )
        } else {
            selectedVertex = null
        }
    }
}
