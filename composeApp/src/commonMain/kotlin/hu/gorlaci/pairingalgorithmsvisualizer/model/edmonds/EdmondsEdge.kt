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
            EdmondsVertexType.OUTER, EdmondsVertexType.ROOT -> {
                when (toVertex.type) {
                    EdmondsVertexType.OUTER, EdmondsVertexType.ROOT -> EdmondsEdgeType.OUTER_OUTER
                    EdmondsVertexType.CLEARING -> EdmondsEdgeType.OUTER_CLEARING
                    EdmondsVertexType.INNER -> EdmondsEdgeType.OUTER_INNER
                    EdmondsVertexType.NONE -> throw IllegalStateException("VertexType.NONE is not allowed when determining edge type")
                }
            }

            EdmondsVertexType.CLEARING -> {
                when (toVertex.type) {
                    EdmondsVertexType.OUTER, EdmondsVertexType.ROOT -> EdmondsEdgeType.OUTER_CLEARING
                    EdmondsVertexType.CLEARING -> EdmondsEdgeType.CLEARING_CLEARING
                    EdmondsVertexType.INNER -> EdmondsEdgeType.CLEARING_INNER
                    EdmondsVertexType.NONE -> throw IllegalStateException("VertexType.NONE is not allowed when determining edge type")
                }
            }

            EdmondsVertexType.INNER -> {
                when (toVertex.type) {
                    EdmondsVertexType.OUTER, EdmondsVertexType.ROOT -> EdmondsEdgeType.OUTER_INNER
                    EdmondsVertexType.CLEARING -> EdmondsEdgeType.CLEARING_INNER
                    EdmondsVertexType.INNER -> EdmondsEdgeType.INNER_INNER
                    EdmondsVertexType.NONE -> throw IllegalStateException("VertexType.NONE is not allowed when determining edge type")
                }
            }

            EdmondsVertexType.NONE -> {
                throw IllegalStateException("VertexType.NONE is not allowed when determining edge type")
            }
        }
}
