package hu.gorlaci.pairingalgorithmsvisualizer.model

interface PairableGraph<VertexType : Vertex, EdgeType : Edge<VertexType>> {
    fun pairVertices(
        vertexA: VertexType,
        vertexB: VertexType,
    )

    fun unpairVertices(
        vertexA: VertexType,
        vertexB: VertexType,
    )

    fun getPair(vertex: VertexType): VertexType?
}
