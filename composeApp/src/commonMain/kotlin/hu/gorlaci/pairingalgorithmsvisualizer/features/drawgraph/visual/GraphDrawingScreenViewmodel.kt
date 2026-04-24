package hu.gorlaci.pairingalgorithmsvisualizer.features.drawgraph.visual

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE

class GraphDrawingScreenViewmodel(
    private val graphStorage: GraphStorage,
) : ViewModel() {
    private var graph =
        Graph(
            name = "Saját Gráf",
            newVertex = { Vertex(it) },
            newEdge = { from, to -> Edge(from, to) },
        )

    val graphicalGraph = mutableStateOf(graph.toGraphicalGraph())

    private var nextID = "A"

    private fun increaseID(index: Int) {
        if (index == -1) {
            nextID = "A$nextID"
        } else if (nextID[index] == 'Z') {
            nextID = nextID.substring(0, index) + "A" + nextID.substring(index + 1)
            increaseID(index - 1)
        } else {
            nextID = nextID.substring(0, index) + (nextID[index] + 1) + nextID.substring(index + 1)
        }
    }

    private fun addVertex(
        x: Double,
        y: Double,
    ) {
        graph.vertices.add(Vertex(nextID))
        graph.idCoordinatesMap[nextID] = Pair(x, y)
        increaseID(nextID.length - 1)
        graphicalGraph.value = graph.toGraphicalGraph()
    }

    val drawMode = mutableStateOf(DrawMode.VERTEX)

    fun changeDrawMode(mode: DrawMode) {
        drawMode.value = mode
        firstVertexForEdge = null
    }

    private var firstVertexForEdge: Vertex? = null

    fun onLeftClick(
        x: Double,
        y: Double,
    ) {
        when (drawMode.value) {
            DrawMode.VERTEX -> {
                if (graph.getVertexByCoordinates(x, y) == null) {
                    addVertex(x, y)
                }
            }

            DrawMode.EDGE -> {
                val clickedVertex = graph.getVertexByCoordinates(x, y)
                if (clickedVertex != null) {
                    if (firstVertexForEdge == null) {
                        firstVertexForEdge = clickedVertex
                        graphicalGraph.value =
                            graphicalGraph.value.changeInnerColor(clickedVertex, LIGHT_ORANGE)
                    } else {
                        if (firstVertexForEdge != clickedVertex) {
                            val clickedEdge = graph.edges.find {
                                (
                                    it.fromVertex == clickedVertex &&
                                        it.toVertex == firstVertexForEdge
                                    ) ||
                                    (
                                        it.toVertex == clickedVertex &&
                                            it.fromVertex == firstVertexForEdge
                                        )
                            }

                            if (clickedEdge != null) {
                                graph.edges.remove(clickedEdge)
                            } else {
                                graph.addEdge(firstVertexForEdge!!.label, clickedVertex.label)
                            }
                        }
                        firstVertexForEdge = null
                        graphicalGraph.value = graph.toGraphicalGraph()
                    }
                }
            }
        }
    }

    var draggedVertex: Vertex? = null

    fun onDragStart(
        x: Double,
        y: Double,
    ) {
        draggedVertex = graph.getVertexByCoordinates(x, y)
        firstVertexForEdge = null
    }

    fun onDrag(
        deltaX: Double,
        deltaY: Double,
    ) {
        draggedVertex?.let { vertex ->
            val originalCoordinates = graph.idCoordinatesMap[vertex.id[0]] ?: Pair(0.0, 0.0)
            graph.idCoordinatesMap[vertex.id[0]] =
                Pair(originalCoordinates.first + deltaX, originalCoordinates.second + deltaY)
            graphicalGraph.value = graph.toGraphicalGraph()
        }
    }

    fun onDragEnd() {
        draggedVertex = null
    }

    fun saveGraph() {
        if (!graphStorage.getAllGraphs().contains(graph)) {
            graphStorage.addGraph(graph)
            graph =
                Graph(
                    name = "Saját Gráf",
                    newVertex = { Vertex(it) },
                    newEdge = { from, to -> Edge(from, to) },
                )
            graphicalGraph.value = graph.toGraphicalGraph()
            graphName.value = "Saját Gráf"
            nextID = "A"
        }
    }

    val graphName = mutableStateOf("Saját Gráf")

    fun onNameChange(newName: String) {
        graphName.value = newName
        graph.name = newName
    }
}

enum class DrawMode {
    VERTEX,
    EDGE,
}
