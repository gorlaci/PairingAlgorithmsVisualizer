package hu.gorlaci.pairingalgorithmsvisualizer.model.egervary

import hu.gorlaci.pairingalgorithmsvisualizer.model.BipartiteGraph

class EgervaryGraph(
    name: String,
    override val vertices: MutableSet<EgervaryVertex> = mutableSetOf(),
    override val edges: MutableSet<EgervaryEdge> = mutableSetOf(),
    idCoordinateMap: MutableMap<String, Pair<Double, Double>> = mutableMapOf(),
) : BipartiteGraph<EgervaryVertex, EgervaryEdge>(
    name = name,
    vertices = vertices,
    edges = edges,
    idCoordinatesMap = idCoordinateMap,
    newVertex = { EgervaryVertex(it) },
    newEdge = { from, to ->
        val edge = EgervaryEdge(from, to, weight = 1)
        from.edges.add(edge)
        to.edges.add(edge)
        edge
    },
) {

    fun runAlgorithm() {
        init()
        findMaximumRedMatching()
        while (!isPairingComplete()) {
            adjustLabels()
            findMaximumRedMatching()
        }
    }

    private fun init() {
        createClasses()
        completeGraph()
        for (edge in edges) {
            edge.selected = false
        }
        for (vertex in class1) {
            vertex.label = vertex.edges.maxOfOrNull { it.weight } ?: 0
        }
        for (vertex in class2) {
            vertex.label = 0
        }
    }

    private fun completeGraph() {
        while (class1.size < class2.size) {
            val newVertex = EgervaryVertex("")
            class1.add(newVertex)
            vertices.add(newVertex)
        }
        while (class2.size < class1.size) {
            val newVertex = EgervaryVertex("")
            class2.add(newVertex)
            vertices.add(newVertex)
        }
        for (vertex1 in class1) {
            for (vertex2 in class2) {
                if (edges.none {
                        (it.toVertex == vertex1 && it.fromVertex == vertex2) ||
                            (it.toVertex == vertex2 && it.fromVertex == vertex1)
                    }
                ) {
                    val edge = EgervaryEdge(vertex1, vertex2, weight = 0)
                    vertex1.edges.add(edge)
                    vertex2.edges.add(edge)
                    edges.add(edge)
                }
            }
        }
    }

    private fun isPairingComplete(): Boolean = vertices.all { it.pair != null }

    private var augmentMade = true

    private fun findMaximumRedMatching() {
        augmentMade = true
        while (augmentMade) {
            findAugmentingPath()
        }
    }

    val unpairedVertices = mutableSetOf<EgervaryVertex>()
    val pairedVertices = mutableSetOf<EgervaryVertex>()
    val visitedVertices = mutableSetOf<EgervaryVertex>()

    private fun findAugmentingPath() {
        augmentMade = false

        unpairedVertices.clear()
        unpairedVertices.addAll(class1.filter { it.pair == null })
        pairedVertices.clear()
        visitedVertices.clear()

        while (unpairedVertices.isNotEmpty()) {
            val unpairedCopy = unpairedVertices.toSet()
            for (vertex in unpairedCopy) {
                visitedVertices.add(vertex)
                for (edge in (vertex.redEdges)) {
                    val neighbour = edge.otherEnd(vertex) ?: continue
                    if (neighbour in visitedVertices) {
                        continue
                    }
                    neighbour.parentEdge = edge
                    visitedVertices.add(neighbour)
                }
                unpairedVertices.remove(vertex)
            }

            val pairedCopy = pairedVertices.toSet()
            for (vertex in pairedCopy) {
                if (vertex.pair == null) {
                    augmentFromVertex(vertex)
                    augmentMade = true
                    reset()
                    return
                }
                vertex.pair?.let { pair ->
                    unpairedVertices.add(pair)
                    pair.parentEdge = pair.edges.first { it.selected }
                }
                pairedVertices.remove(vertex)
            }
        }
    }

    private fun augmentFromVertex(vertex: EgervaryVertex) {
        var current: EgervaryVertex = vertex
        while (current.parentEdge != null) {
            val parentEdge = current.parentEdge ?: break
            parentEdge.selected = !parentEdge.selected
            current = parentEdge.otherEnd(current) ?: break
        }
    }

    private fun reset() {
        for (vertex in vertices) {
            vertex.parentEdge = null
        }
    }

    private fun adjustLabels() {
        val u = class1.filter { it.pair == null }
        val tComma = class2.filter { it in visitedVertices }
        val t = tComma.mapNotNull { it.parentEdge?.otherEnd(it) }
        val filteredClass1 = t + u
        val filteredClass2 = class2 - tComma.toSet()
        val delta =
            edges.filter {
                (it.toVertex in filteredClass1 && it.fromVertex in filteredClass2) ||
                    (it.fromVertex in filteredClass1 && it.toVertex in filteredClass2)
            }.minOfOrNull { it.fromVertex.label + it.toVertex.label - it.weight } ?: 0
        for (vertex in (t + u)) {
            vertex.label -= delta
        }
        for (vertex in tComma) {
            vertex.label += delta
        }
    }
}
