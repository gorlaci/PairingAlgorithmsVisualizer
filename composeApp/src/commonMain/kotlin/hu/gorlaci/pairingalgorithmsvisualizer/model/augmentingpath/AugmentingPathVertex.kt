package hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath

import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex

class AugmentingPathVertex(
    id: String,
    val neighbours: MutableSet<AugmentingPathVertex>,
    var visited: Boolean = false,
    var pair: AugmentingPathVertex? = null,
    var parent: AugmentingPathVertex? = null,
) : Vertex(id)