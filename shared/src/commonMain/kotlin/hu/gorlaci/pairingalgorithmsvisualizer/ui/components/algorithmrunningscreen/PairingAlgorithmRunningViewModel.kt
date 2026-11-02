package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.PairableGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE

abstract class PairingAlgorithmRunningViewModel<
    GraphType,
    VertexType : Vertex,
    EdgeType : Edge<VertexType>,
    > : AlgorithmRunningViewModel<GraphType, VertexType, EdgeType>()
    where GraphType : Graph<VertexType, EdgeType>, GraphType : PairableGraph<VertexType, EdgeType> {

    override var selectedVertex: VertexType? = null

    override fun onTap(position: Vector2D) {
        if (!inSetup.value) return

        val graph = selectedGraph.value
        val clickedVertex = graph.getVertexByCoordinates(position) ?: return
        if (selectedVertex == null) {
            selectedVertex = clickedVertex
            graphicalGraph.value = graphicalGraph.value.changeVertexInnerColor(
                clickedVertex,
                LIGHT_ORANGE,
            )
            return
        }
        if (selectedVertex == clickedVertex) {
            selectedVertex = null
            graphicalGraph.value = graphicalGraph.value.changeVertexInnerColor(
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
