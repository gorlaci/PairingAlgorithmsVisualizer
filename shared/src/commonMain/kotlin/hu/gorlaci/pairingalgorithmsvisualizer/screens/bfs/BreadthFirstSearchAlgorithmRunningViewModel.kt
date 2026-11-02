package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.BreadthFirstSearchGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.BreadthFirstSearchVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.toBreadthFirstSearchGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph

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

    open val tree: State<GraphicalGraph> = derivedStateOf {
        bfsSteps.value[step.value].third.toGraphicalGraph()
    }

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

    override fun onTap(position: Vector2D) {
        if (!inSetup.value) return

        val graph = currentGraph.value
        val clickedVertex = graph.getVertexByCoordinates(position) ?: return

        selectedVertex?.let {
            graphicalGraph.value = graphicalGraph.value.changeVertexInnerColor(
                it,
                Color.White,
            )
        }

        if (selectedVertex == clickedVertex) {
            selectedVertex = null
            return
        }
        selectedVertex = clickedVertex
        graphicalGraph.value = graphicalGraph.value.changeVertexInnerColor(
            clickedVertex,
            LIGHT_ORANGE,
        )
    }
}
