package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.*
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE

abstract class PairingAlgorithmRunningViewModel<
    GraphType,
    VertexType : Vertex,
    EdgeType : Edge<VertexType>,
    >(
    graphStorage: GraphStorage,
) : AlgorithmRunningViewModel<GraphType, VertexType, EdgeType>(
    graphStorage = graphStorage,
)where GraphType : Graph<VertexType, EdgeType>, GraphType : PairableGraph<VertexType, EdgeType> {

    abstract override val graphList: List<GraphType>

    override val selectedGraph = derivedStateOf { graphList[selectedGraphIndex.value] }

    override var selectedVertex: VertexType? = null

    override fun onTap(
        x: Double,
        y: Double,
    ) {
        if (!inSetup.value) return

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
                graph.unpairVertices(clickedVertex, selectedVertexNotNull)
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
