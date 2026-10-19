package hu.gorlaci.pairingalgorithmsvisualizer.ui.model

import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.StepType

data class GraphicalGraph(
    val graphicalVertices: List<GraphicalVertex> = emptyList(),
    val graphicalEdges: List<GraphicalEdge> = emptyList(),
    val stepType: StepType = StepType.Nothing(),
) {
    fun changeVertexInnerColor(
        vertex: Vertex,
        color: Color,
    ): GraphicalGraph {
        val graphicalVertex = graphicalVertices.find { it.name == vertex.name }
        if (graphicalVertex == null) {
            return this
        }
        val newGraphicalVertex = graphicalVertex.copy(innerColor = color)
        val newGraphicalVertices = graphicalVertices - graphicalVertex + newGraphicalVertex
        return this.copy(graphicalVertices = newGraphicalVertices)
    }

    fun addEdgeHighlight(
        edge: Edge<out Vertex>,
        color: Color,
    ): GraphicalGraph {
        val graphicalEdge =
            graphicalEdges.find {
                it.startGraphicalVertex.name == edge.fromVertex.name &&
                    it.endGraphicalVertex.name == edge.toVertex.name
            } ?: return this
        val newGraphicalEdge = graphicalEdge.copy(highlight = color)
        val newGraphicalEdges = graphicalEdges - graphicalEdge + newGraphicalEdge
        return this.copy(graphicalEdges = newGraphicalEdges)
    }

    fun addEdgeHighlight(
        fromVertex: Vertex,
        toVertex: Vertex,
        color: Color,
    ): GraphicalGraph {
        val graphicalEdge = graphicalEdges.find {
            (
                it.startGraphicalVertex.name == fromVertex.name &&
                    it.endGraphicalVertex.name == toVertex.name
                ) ||
                (
                    it.startGraphicalVertex.name == toVertex.name &&
                        it.endGraphicalVertex.name == fromVertex.name
                    )
        } ?: return this
        val newGraphicalEdge = graphicalEdge.copy(highlight = color)
        val newGraphicalEdges = graphicalEdges - graphicalEdge + newGraphicalEdge
        return this.copy(graphicalEdges = newGraphicalEdges)
    }

    fun removeEdgeHighlight(edge: Edge<out Vertex>) = addEdgeHighlight(edge, Color.Transparent)

    fun removeEdgeHighlight(
        fromVertex: Vertex,
        toVertex: Vertex,
    ) = addEdgeHighlight(fromVertex, toVertex, Color.Transparent)

    fun removeAllEdgeHighlights(): GraphicalGraph {
        val newGraphicalEdges =
            graphicalEdges.map {
                it.copy(
                    highlight = Color.Transparent,
                )
            }
        return this.copy(
            graphicalEdges = newGraphicalEdges,
        )
    }

    fun animateBlossomVertices(
        blossomVertices: Set<EdmondsVertex>,
        originalGraph: EdmondsGraph,
        animationProgress: Float,
    ): GraphicalGraph {
        val blossomX =
            blossomVertices.sumOf { originalGraph.getVertexCoordinates(it).first * it.id.size } /
                blossomVertices.sumOf { it.id.size }
        val blossomY =
            blossomVertices.sumOf { originalGraph.getVertexCoordinates(it).second * it.id.size } /
                blossomVertices.sumOf { it.id.size }

        val newGraphicalVertices = mutableMapOf<GraphicalVertex, GraphicalVertex>()
        for (vertex in blossomVertices) {
            val graphicalVertex = graphicalVertices.find { it.name == vertex.name } ?: continue
            val newX =
                originalGraph.getVertexCoordinates(vertex).first +
                    (blossomX - originalGraph.getVertexCoordinates(vertex).first) *
                    animationProgress
            val newY =
                originalGraph.getVertexCoordinates(vertex).second + (
                    blossomY -
                        originalGraph
                            .getVertexCoordinates(
                                vertex,
                            ).second
                    ) * animationProgress
            val newGraphicalVertex =
                graphicalVertex.copy(
                    x = newX,
                    y = newY,
                )
            newGraphicalVertices[graphicalVertex] = newGraphicalVertex
        }

        val newGraphicalEdges =
            graphicalEdges.map { graphicalEdge ->
                val newStartVertex = newGraphicalVertices[graphicalEdge.startGraphicalVertex]
                val newEndVertex = newGraphicalVertices[graphicalEdge.endGraphicalVertex]
                if (newStartVertex != null || newEndVertex != null) {
                    graphicalEdge.copy(
                        startGraphicalVertex = newStartVertex ?: graphicalEdge.startGraphicalVertex,
                        endGraphicalVertex = newEndVertex ?: graphicalEdge.endGraphicalVertex,
                    )
                } else {
                    graphicalEdge
                }
            }

        return this.copy(
            graphicalVertices = graphicalVertices.map { gv -> newGraphicalVertices[gv] ?: gv },
            graphicalEdges = newGraphicalEdges,
        )
    }

    fun changeVertexHighlight(
        vertex: Vertex,
        color: Color,
        highlightType: HighlightType = HighlightType.CIRCLE,
    ): GraphicalGraph {
        val graphicalVertex = graphicalVertices.find { it.name == vertex.name } ?: return this
        val newGraphicalVertex = graphicalVertex.copy(
            highlight = color,
            highlightType = highlightType,
        )
        val newGraphicalVertices = graphicalVertices - graphicalVertex + newGraphicalVertex
        return this.copy(graphicalVertices = newGraphicalVertices)
    }

    fun removeVertex(vertex: Vertex): GraphicalGraph {
        val newGraphicalVertices = graphicalVertices.filter { it.name != vertex.name }
        val newGraphicalEdges =
            graphicalEdges.filterNot {
                it.startGraphicalVertex.name == vertex.name ||
                    it.endGraphicalVertex.name == vertex.name
            }
        return this.copy(
            graphicalVertices = newGraphicalVertices,
            graphicalEdges = newGraphicalEdges,
        )
    }

    fun removeEdge(
        fromVertex: Vertex,
        toVertex: Vertex,
    ): GraphicalGraph {
        val newGraphicalEdges = graphicalEdges.filterNot {
            (it.startGraphicalVertex.name == fromVertex.name && it.endGraphicalVertex.name == toVertex.name) ||
                (it.startGraphicalVertex.name == toVertex.name && it.endGraphicalVertex.name == fromVertex.name)
        }
        return this.copy(graphicalEdges = newGraphicalEdges)
    }

    fun removeEdge(edge: Edge<out Vertex>) = removeEdge(edge.fromVertex, edge.toVertex)
}
