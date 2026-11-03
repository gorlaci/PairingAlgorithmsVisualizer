package hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.DepthFirstSearchGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.DepthFirstSearchVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.toDepthFirstSearchGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_PURPLE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningViewModel

open class DepthFirstSearchAlgorithmRunningViewModel(
    graphsStorage: GraphStorage,
) : AlgorithmRunningViewModel<
    DepthFirstSearchGraph,
    DepthFirstSearchVertex,
    Edge<DepthFirstSearchVertex>,
    >() {

    override val graphList = derivedStateOf {
        graphsStorage.getAllGraphs().value.map { it.toDepthFirstSearchGraph() }
    }

    override val initString = "Jelöld ki a kezdő csúcsot!"

    private val _steps = mutableStateOf(
        listOf(
            Triple<DepthFirstSearchGraph, StepType, DepthFirstSearchGraph>(
                selectedGraph.value,
                StepType.Nothing(initString),
                DepthFirstSearchGraph(directed = selectedGraph.value.directed),
            ),
        ),
    )

    override val steps = derivedStateOf {
        _steps.value.map { it.first.toGraphicalGraph(it.second) }
    }

    val currentGraph = derivedStateOf {
        _steps.value[step.value].first
    }

    val tree = derivedStateOf {
        _steps.value[step.value].third.toGraphicalGraph()
    }

    val isDirected = derivedStateOf {
        selectedGraph.value.directed
    }

    override fun onGraphSelected(index: Int) {
        selectedGraphIndex.value = index

        selectedGraph.value.resetAlgorithm()

        step.value = 0

        _steps.value = listOf(
            Triple(
                selectedGraph.value,
                StepType.Nothing(initString),
                DepthFirstSearchGraph(directed = selectedGraph.value.directed),
            ),
        )

        graphicalGraph.value = steps.value[0]

        inSetup.value = true
    }

    override fun onRun() {
        val graph = selectedGraph.value

        graph.runAlgorithm(selectedVertex)

        _steps.value = graph.steps

        step.value = 0

        graphicalGraph.value = steps.value[0]

        inSetup.value = false
    }

    override val selectedVertexColor = LIGHT_PURPLE
}
