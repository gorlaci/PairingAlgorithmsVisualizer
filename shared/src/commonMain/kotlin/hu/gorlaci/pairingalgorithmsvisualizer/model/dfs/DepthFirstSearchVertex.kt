package hu.gorlaci.pairingalgorithmsvisualizer.model.dfs

class DepthFirstSearchVertex(
    id: List<String>,
    val neighbours: MutableSet<DepthFirstSearchVertex> = mutableSetOf(),
    var parent: DepthFirstSearchVertex? = null,
    var row: Int = -1,
    var reachedNumber: Int? = null,
    var finishedNumber: Int? = null,
) : hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex(id) {
    constructor(
        id: String,
        neighbours: MutableSet<DepthFirstSearchVertex> = mutableSetOf(),
        parent: DepthFirstSearchVertex? = null,
        reachedNumber: Int? = null,
        finishedNumber: Int? = null,
        row: Int = -1,
    ) : this(
        id = listOf(id),
        neighbours = neighbours,
        parent = parent,
        reachedNumber = reachedNumber,
        finishedNumber = finishedNumber,
        row = row,
    )
}
