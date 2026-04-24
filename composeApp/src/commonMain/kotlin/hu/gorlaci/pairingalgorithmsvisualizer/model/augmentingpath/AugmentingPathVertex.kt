package hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath

import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex

class AugmentingPathVertex(
    id: List<String>,
    val neighbours: MutableSet<AugmentingPathVertex> = mutableSetOf(),
    var visited: Boolean = false,
    var pair: AugmentingPathVertex? = null,
    var parent: AugmentingPathVertex? = null,
) : Vertex(id) {
    constructor(
        id: String,
        neighbours: MutableSet<AugmentingPathVertex> = mutableSetOf(),
        visited: Boolean = false,
        pair: AugmentingPathVertex? = null,
        parent: AugmentingPathVertex? = null,
    ) : this(
        id = listOf(id),
        neighbours = neighbours,
        visited = visited,
        pair = pair,
        parent = parent,
    )
}
