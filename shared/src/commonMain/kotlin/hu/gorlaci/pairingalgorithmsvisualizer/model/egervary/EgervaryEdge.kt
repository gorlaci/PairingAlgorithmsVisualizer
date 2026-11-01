package hu.gorlaci.pairingalgorithmsvisualizer.model.egervary

import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge

class EgervaryEdge(
    override val fromVertex: EgervaryVertex,
    override val toVertex: EgervaryVertex,
    val weight: Int,
) : Edge<EgervaryVertex>(
    fromVertex,
    toVertex,
) {
    val isRed: Boolean
        get() = fromVertex.label + toVertex.label == weight

    var selected = false
}
