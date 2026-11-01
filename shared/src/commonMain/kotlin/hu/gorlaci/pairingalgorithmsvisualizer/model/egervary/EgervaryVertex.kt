package hu.gorlaci.pairingalgorithmsvisualizer.model.egervary

import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex

class EgervaryVertex(
    id: List<String>,
    val edges: MutableList<EgervaryEdge> = mutableListOf(),
    var label: Int = 0,
) : Vertex(
    id = id,
) {
    constructor(
        id: String,
        edges: MutableList<EgervaryEdge> = mutableListOf(),
        label: Int = 0,
    ) : this(
        id = listOf(id),
        edges = edges,
        label = label,
    )

    val redEdges: List<EgervaryEdge>
        get() = edges.filter { it.isRed }

    var parentEdge: EgervaryEdge? = null

    val pair: EgervaryVertex?
        get() = edges.find { it.selected }?.otherEnd(this)
}
