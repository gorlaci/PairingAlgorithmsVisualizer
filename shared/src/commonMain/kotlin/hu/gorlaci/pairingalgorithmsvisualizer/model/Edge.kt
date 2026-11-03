package hu.gorlaci.pairingalgorithmsvisualizer.model

open class Edge<VertexType : Vertex>(
    val fromVertex: VertexType,
    val toVertex: VertexType,
) {
    fun otherEnd(vertex: Vertex): VertexType? {
        if (vertex == fromVertex) {
            return toVertex
        }
        if (vertex == toVertex) {
            return fromVertex
        }
        return null
    }
}
