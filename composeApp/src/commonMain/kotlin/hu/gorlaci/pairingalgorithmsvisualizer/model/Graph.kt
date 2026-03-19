package hu.gorlaci.pairingalgorithmsvisualizer.model

import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex

open class Graph<VertexType : Vertex, EdgeType : Edge>(
    var name: String = "",
    open val vertices: MutableSet<VertexType> = mutableSetOf(),
    open val edges: MutableSet<EdgeType> = mutableSetOf(),
    val idCoordinatesMap: MutableMap<Char, Pair<Double, Double>> = mutableMapOf(),
    private val newVertex: (String) -> VertexType,
    private val newEdge: (VertexType, VertexType) -> EdgeType,
) {
    val isBipartite: Boolean
        get() {
            if (vertices.size < 3) {
                return true
            }
            val unvisited = vertices.toMutableSet()
            val queue = ArrayDeque<Vertex>()
            val colorMap = mutableMapOf<Vertex, Boolean>()
            while (unvisited.isNotEmpty()) {
                val start = unvisited.first()
                queue.add(start)
                colorMap[start] = true
                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst()
                    unvisited.remove(current)
                    val currentColor = colorMap[current] ?: continue
                    val neighbours =
                        edges.filter { it.fromVertex == current && it.toVertex in unvisited }.map { it.toVertex } +
                                edges.filter { it.toVertex == current && it.fromVertex in unvisited }
                                    .map { it.fromVertex }
                    for (neighbour in neighbours) {
                        if (neighbour in colorMap) {
                            if (colorMap[neighbour] == currentColor) {
                                return false
                            }
                        } else {
                            colorMap[neighbour] = !currentColor
                            queue.add(neighbour)
                        }
                    }
                }
            }
            return true
        }

    fun getVertexCoordinates(vertex: Vertex): Pair<Double, Double> =
        Pair(
            vertex.id.map { char ->
                idCoordinatesMap[char]?.first ?: 0.0
            }.sum() / vertex.id.length,
            vertex.id.map { char ->
                idCoordinatesMap[char]?.second ?: 0.0
            }.sum() / vertex.id.length
        )

    open fun toGraphicalGraph(stepType: StepType = StepType()): GraphicalGraph {

        throw NotImplementedError("toGraphicalGraph is not implemented for this graph type")

        val graphicalVertices = vertices.map { vertex ->
            val coordinates = getVertexCoordinates(vertex)
            GraphicalVertex(
                label = vertex.id,
                x = coordinates.first,
                y = coordinates.second,
            )
        }

        val graphicalEdges = edges.map { edge ->
            val fromVertex = graphicalVertices.first { it.label == edge.fromVertex.id }
            val toVertex = graphicalVertices.first { it.label == edge.toVertex.id }
            GraphicalEdge(
                startGraphicalVertex = fromVertex,
                endGraphicalVertex = toVertex,
            )
        }
        return GraphicalGraph(
            graphicalVertices = graphicalVertices,
            graphicalEdges = graphicalEdges,
            stepType = StepType("")
        )
    }

    fun getVertexByCoordinates(
        x: Double,
        y: Double,
    ): VertexType? {
        try {
            return vertices.last { vertex ->
                val coordinates = getVertexCoordinates(vertex)
                val dx = coordinates.first - x
                val dy = coordinates.second - y
                return@last dx * dx + dy * dy <= 400.0
            }
        } catch (_: NoSuchElementException) {
            return null
        }
    }

    fun addEdge(
        fromId: String,
        toId: String,
    ) {
        val fromVertex = vertices.find { it.id == fromId }
        val toVertex = vertices.find { it.id == toId }
        if (fromVertex != null && toVertex != null) {
            val newEdge = newEdge(fromVertex, toVertex)
            edges.add(newEdge)
        }
    }
}