package hu.gorlaci.pairingalgorithmsvisualizer.util

import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsEdge

fun containsSameEdges(
    a: Collection<EdmondsEdge>,
    b: Collection<EdmondsEdge>,
) = a.all { edgeA ->
    b.any { edgeB ->
        edgeA.fromVertex.id == edgeB.fromVertex.id &&
            edgeA.toVertex.id == edgeB.toVertex.id
    }
} &&
    b.all { edgeB ->
        a.any { edgeA ->
            edgeA.fromVertex.id == edgeB.fromVertex.id &&
                edgeA.toVertex.id == edgeB.toVertex.id
        }
    }
