package hu.gorlaci.pairingalgorithmsvisualizer.model.bfs

class BreadthFirstSearchVertex(
    id: List<String>,
    val neighbours: MutableSet<BreadthFirstSearchVertex> = mutableSetOf(),
    var parent: BreadthFirstSearchVertex? = null,
    var distance: Int? = null,
    var row: Int = -1,
) : hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex(id) {
    constructor(
        id: String,
        neighbours: MutableSet<BreadthFirstSearchVertex> = mutableSetOf(),
        parent: BreadthFirstSearchVertex? = null,
        distance: Int? = null,
        row: Int = -1,
    ) : this(
        id = listOf(id),
        neighbours = neighbours,
        parent = parent,
        distance = distance,
        row = row,
    )
}
