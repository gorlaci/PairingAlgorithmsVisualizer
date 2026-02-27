package hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds

import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.quiz.EdmondsEdgeType

class EdmondsEdge(
    override val fromVertex: EdmondsVertex,
    override val toVertex: EdmondsVertex,
    var visited: Boolean = false,
): Edge(fromVertex, toVertex) {
    override fun toString(): String = "Edge(${fromVertex.id}, ${toVertex.id})"

    fun getType() =
        when (fromVertex.type) {
            VertexType.OUTER, VertexType.ROOT -> {
                when (toVertex.type) {
                    VertexType.OUTER, VertexType.ROOT -> EdmondsEdgeType.OUTER_OUTER
                    VertexType.CLEARING -> EdmondsEdgeType.OUTER_CLEARING
                    VertexType.INNER -> EdmondsEdgeType.OUTER_INNER
                    VertexType.NONE -> throw IllegalStateException("VertexType.NONE is not allowed when determining edge type")
                }
            }

            VertexType.CLEARING -> {
                when (toVertex.type) {
                    VertexType.OUTER, VertexType.ROOT -> EdmondsEdgeType.OUTER_CLEARING
                    VertexType.CLEARING -> EdmondsEdgeType.CLEARING_CLEARING
                    VertexType.INNER -> EdmondsEdgeType.CLEARING_INNER
                    VertexType.NONE -> throw IllegalStateException("VertexType.NONE is not allowed when determining edge type")
                }
            }

            VertexType.INNER -> {
                when (toVertex.type) {
                    VertexType.OUTER, VertexType.ROOT -> EdmondsEdgeType.OUTER_INNER
                    VertexType.CLEARING -> EdmondsEdgeType.CLEARING_INNER
                    VertexType.INNER -> EdmondsEdgeType.INNER_INNER
                    VertexType.NONE -> throw IllegalStateException("VertexType.NONE is not allowed when determining edge type")
                }
            }

            VertexType.NONE -> {
                throw IllegalStateException("VertexType.NONE is not allowed when determining edge type")
            }
        }
}
