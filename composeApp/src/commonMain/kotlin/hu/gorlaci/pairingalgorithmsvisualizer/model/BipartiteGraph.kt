package hu.gorlaci.pairingalgorithmsvisualizer.model

abstract class BipartiteGraph<VertexType : Vertex, EdgeType : Edge<VertexType>>(
    name: String,
    vertices: MutableSet<VertexType> = mutableSetOf(),
    edges: MutableSet<EdgeType> = mutableSetOf(),
    idCoordinatesMap: MutableMap<String, Pair<Double, Double>> = mutableMapOf(),
    newVertex: (String) -> VertexType,
    newEdge: (VertexType, VertexType) -> EdgeType,
) : Graph<VertexType, EdgeType>(
    name,
    vertices,
    edges,
    idCoordinatesMap,
    newVertex,
    newEdge,
) {
    val class1 = mutableSetOf<VertexType>()
    val class2 = mutableSetOf<VertexType>()

    fun createClasses() {
        val unvisited = vertices.toMutableSet()
        while (unvisited.isNotEmpty()) {
            val vertex = unvisited.first()
            unvisited.remove(vertex)
            createClassesRecursive(vertex, class1, class2, unvisited)
        }
    }

    private fun createClassesRecursive(
        vertex: VertexType,
        currentClass: MutableSet<VertexType>,
        otherClass: MutableSet<VertexType>,
        unvisited: MutableSet<VertexType>,
    ) {
        currentClass.add(vertex)
        for (neighbour in getNeighbours(vertex)) {
            if (neighbour in unvisited) {
                unvisited.remove(neighbour)
                createClassesRecursive(neighbour, otherClass, currentClass, unvisited)
            }
        }
    }
}
