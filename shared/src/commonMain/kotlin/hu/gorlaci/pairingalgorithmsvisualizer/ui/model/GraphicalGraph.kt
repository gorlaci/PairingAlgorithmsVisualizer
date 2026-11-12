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
        val blossomCoordinates = blossomVertices.map {
            originalGraph.getVertexCoordinates(it) * it.id.size
        }.reduce { d, acc ->
            d + acc
        } / blossomVertices.sumOf { it.id.size }

        val newGraphicalVertices = mutableMapOf<GraphicalVertex, GraphicalVertex>()
        for (vertex in blossomVertices) {
            val graphicalVertex = graphicalVertices.find { it.name == vertex.name } ?: continue

            val newPosition =
                originalGraph.getVertexCoordinates(vertex) +
                    (blossomCoordinates - originalGraph.getVertexCoordinates(vertex)) * animationProgress

            val newGraphicalVertex =
                graphicalVertex.copy(
                    position = newPosition,
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

    fun addEdge(
        fromVertex: Vertex,
        toVertex: Vertex,
        selected: Boolean = false,
        highlight: Color = Color.Transparent,
        color: Color = Color.Black,
        label: String? = null,
    ): GraphicalGraph {
        val startGraphicalVertex = graphicalVertices.find { it.name == fromVertex.name } ?: return this
        val endGraphicalVertex = graphicalVertices.find { it.name == toVertex.name } ?: return this
        val newGraphicalEdge = GraphicalEdge(
            startGraphicalVertex = startGraphicalVertex,
            endGraphicalVertex = endGraphicalVertex,
            selected = selected,
            highlight = highlight,
            color = color,
            label = label,
        )
        val newGraphicalEdges = graphicalEdges + newGraphicalEdge

        return this.copy(graphicalEdges = newGraphicalEdges)
    }

    fun changeEdge(
        fromVertex: Vertex,
        toVertex: Vertex,
        selected: Boolean? = null,
        highlight: Color? = null,
        color: Color? = null,
        label: String? = null,
    ): GraphicalGraph {
        val graphicalEdge =
            graphicalEdges.find {
                (it.startGraphicalVertex.name == fromVertex.name && it.endGraphicalVertex.name == toVertex.name) ||
                    (it.startGraphicalVertex.name == toVertex.name && it.endGraphicalVertex.name == fromVertex.name)
            } ?: return this
        val newGraphicalEdge =
            graphicalEdge.copy(
                selected = selected ?: graphicalEdge.selected,
                highlight = highlight ?: graphicalEdge.highlight,
                color = color ?: graphicalEdge.color,
                label = label ?: graphicalEdge.label,
            )
        val newGraphicalEdges = buildList {
            addAll(graphicalEdges)
            remove(graphicalEdge)
            add(newGraphicalEdge)
        }
        return this.copy(graphicalEdges = newGraphicalEdges)
    }

    fun changeEdge(
        edge: Edge<out Vertex>,
        selected: Boolean? = null,
        highlight: Color? = null,
        color: Color? = null,
        label: String? = null,
    ): GraphicalGraph = changeEdge(edge.fromVertex, edge.toVertex, selected, highlight, color, label)

    fun changeVertex(
        vertex: Vertex,
        innerColor: Color? = null,
        highlight: Color? = null,
        highlightType: HighlightType? = null,
        label: String? = null,
    ): GraphicalGraph {
        val graphicalVertex = graphicalVertices.find { it.name == vertex.name } ?: return this
        val newGraphicalVertex = GraphicalVertex(
            name = vertex.name,
            position = graphicalVertex.position,
            innerColor = innerColor ?: graphicalVertex.innerColor,
            highlight = highlight ?: graphicalVertex.highlight,
            highlightType = highlightType ?: graphicalVertex.highlightType,
            label = label ?: graphicalVertex.label,
        )
        val newGraphicalVertices = buildList {
            addAll(graphicalVertices)
            remove(graphicalVertex)
            add(newGraphicalVertex)
        }
        return this.copy(graphicalVertices = newGraphicalVertices)
    }
}
