package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.BreadthFirstSearchGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.BreadthFirstSearchVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.toBreadthFirstSearchGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningViewModel

class BreadthFirstSearchAlgorithmRunningViewModel(
    graphsStorage: GraphStorage,
) : AlgorithmRunningViewModel(graphsStorage) {

    override val graphList = graphsStorage.getAllGraphs().map { it.toBreadthFirstSearchGraph() }
    override val selectedGraph = derivedStateOf {
        graphList[selectedGraphIndex.value]
    }

    private val _steps = mutableStateOf(
        listOf(
            Triple<BreadthFirstSearchGraph, StepType, BreadthFirstSearchGraph>(
                selectedGraph.value,
                StepType.Nothing(),
                BreadthFirstSearchGraph(),
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

    override fun onGraphSelected(index: Int) {
        selectedGraphIndex.value = index

        selectedGraph.value.resetAlgorithm()

        step.value = 0

        _steps.value = listOf(
            Triple(
                selectedGraph.value,
                StepType.Nothing(),
                BreadthFirstSearchGraph(),
            ),
        )

        graphicalGraph.value = steps.value[0]

        inSetup.value = true
    }

    override fun onRun() {
        val graph = selectedGraph.value

        graph.runAlgorithm(selectedVertex as? BreadthFirstSearchVertex)

        _steps.value = graph.steps

        step.value = 0

        graphicalGraph.value = steps.value[0]

        inSetup.value = false
    }

    override fun onTap(
        x: Double,
        y: Double,
    ) {
        if (!inSetup.value) return

        val graph = currentGraph.value
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
    }
}
