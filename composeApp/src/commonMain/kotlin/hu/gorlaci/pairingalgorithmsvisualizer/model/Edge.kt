package hu.gorlaci.pairingalgorithmsvisualizer.model

open class Edge<VertexType : Vertex>(
    open val fromVertex: VertexType,
    open val toVertex: VertexType,
) {
    fun otherEnd(vertex: VertexType): VertexType? {
        if (vertex == fromVertex) {
            return toVertex
        }
        if (vertex == toVertex) {
            return fromVertex
        }
        return null
    }
}
