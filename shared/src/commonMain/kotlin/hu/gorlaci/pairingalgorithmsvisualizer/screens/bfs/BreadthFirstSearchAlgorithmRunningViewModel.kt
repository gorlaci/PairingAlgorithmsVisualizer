package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.BreadthFirstSearchGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.BreadthFirstSearchVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.quiz.BreadthFirstSearchStepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.quiz.NeighbourStatus
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.toBreadthFirstSearchGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_PINK
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_PURPLE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningViewModel

open class BreadthFirstSearchAlgorithmRunningViewModel(
    graphsStorage: GraphStorage,
) : AlgorithmRunningViewModel<
    BreadthFirstSearchGraph,
    BreadthFirstSearchVertex,
    Edge<BreadthFirstSearchVertex>,
    >() {

    override val graphList = derivedStateOf {
        graphsStorage.getAllGraphs().value.map { it.toBreadthFirstSearchGraph() }
    }

    override val initString = "Jelöld ki a kezdő csúcsot!"

    protected val bfsSteps = mutableStateOf(
        listOf(
            Triple<BreadthFirstSearchGraph, StepType, BreadthFirstSearchGraph>(
                selectedGraph.value,
                StepType.Nothing(initString),
                BreadthFirstSearchGraph(directed = selectedGraph.value.directed),
            ),
        ),
    )

    override val steps = derivedStateOf {
        bfsSteps.value.map { it.first.toGraphicalGraph(it.second) }
    }

    val currentGraph = derivedStateOf {
        bfsSteps.value[step.value].first
    }

    val tree = mutableStateOf(
        bfsSteps.value[step.value].third.toGraphicalGraph(),
    )

    val isDirected = derivedStateOf {
        currentGraph.value.directed
    }

    val showDistances = mutableStateOf(false)

    override fun onGraphSelected(index: Int) {
        selectedGraphIndex.value = index

        selectedGraph.value.resetAlgorithm()

        step.value = 0

        bfsSteps.value = listOf(
            Triple(
                selectedGraph.value,
                StepType.Nothing(initString),
                BreadthFirstSearchGraph(directed = selectedGraph.value.directed),
            ),
        )

        graphicalGraph.value = steps.value[0]

        inSetup.value = true
    }

    override fun onRun() {
        val graph = selectedGraph.value

        graph.runAlgorithm(selectedVertex)

        showDistances.value = selectedVertex != null

        bfsSteps.value = graph.steps

        step.value = 0

        graphicalGraph.value = steps.value[0]

        inSetup.value = false
    }

    override val selectedVertexColor = LIGHT_PURPLE

    override fun setGraphicalGraph() {
        super.setGraphicalGraph()
        tree.value = bfsSteps.value[step.value].third.toGraphicalGraph()

        val currentStep = bfsSteps.value[step.value].second
        if (currentStep is BreadthFirstSearchStepType.NeighbourEvaluated &&
            currentStep.neighbourStatus == NeighbourStatus.ADDED
        ) {
            tree.value = tree.value.addEdge(currentStep.vertex, currentStep.neighbour)
            graphicalGraph.value = graphicalGraph.value
                .changeVertexInnerColor(
                    currentStep.neighbour,
                    LIGHT_PINK,
                )
        }
    }

    enum class DisplayMode {
        Visual,
        NeighbourList,
    }

    val displayMode = mutableStateOf(DisplayMode.Visual)

    fun onDisplayModeChanged(newDisplayMode: DisplayMode) {
        displayMode.value = newDisplayMode
    }
}
