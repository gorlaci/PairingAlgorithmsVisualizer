package hu.gorlaci.pairingalgorithmsvisualizer.model

open class Edge(
    open val fromVertex: Vertex,
    open val toVertex: Vertex,
) {
    companion object {
        fun fromVertices(vertex1: Vertex, vertex2: Vertex): Edge =
            Edge(vertex1, vertex2)
    }
}