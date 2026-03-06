package hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath

import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.quiz.AugmentingStepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.DARK_GREEN
import hu.gorlaci.pairingalgorithmsvisualizer.ui.ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.HighlightType

class AugmentingPathGraph(
    override val vertices: MutableSet<AugmentingPathVertex> = mutableSetOf(),
    name: String = "",
    idCoordinatesMap: MutableMap<Char, Pair<Double, Double>>
) : Graph(
    name = name,
    vertices = vertices,
    edges = mutableSetOf(),
    idCoordinatesMap = idCoordinatesMap
) {


    val steps = mutableListOf<GraphicalGraph>()

    private fun saveStep(stepType: AugmentingStepType = AugmentingStepType.Nothing()) {
        steps.add(toGraphicalGraph(stepType))
        println("Saved step: ${stepType.description}")
    }

    fun runAlgorithm() {
        saveStep()
        saveStep(AugmentingStepType.Nothing("Kiindulunk az üres párosításból"))
        var nextVertex = getNextVertex()
        while (nextVertex != null) {
            findAugmentingPath(nextVertex)
            nextVertex = getNextVertex()
        }
        reset()
        saveStep(AugmentingStepType.Nothing("megvizsgáltunk minden párosítatlan csúcsot, kész a maximális párosítás"))
    }

    private val unpairedVertices = mutableSetOf<AugmentingPathVertex>()
    private val pairedVertices = mutableSetOf<AugmentingPathVertex>()

    fun findAugmentingPath(startVertex: AugmentingPathVertex) {

        println("Finding augmenting path from vertex ${startVertex.id}")

        unpairedVertices.clear()
        unpairedVertices.add(startVertex)

        pairedVertices.clear()

        while (unpairedVertices.isNotEmpty()) {
            for (vertex in unpairedVertices) {
                vertex.visited = true
                saveStep(AugmentingStepType.Nothing("Vizsgáljuk ${vertex.id} csúcs szomszédait"))
                for (neighbour in vertex.neighbours.filter { !it.visited }) {
                    neighbour.parent = vertex
                    saveStep(AugmentingStepType.Nothing("Vizsgáljuk ${neighbour.id} csúcsot"))
                    if (neighbour.pair == null) {
                        saveStep(AugmentingStepType.Nothing("Nincs párja, javítóutat találtunk"))
                        augmentFromVertex(neighbour)
                        reset()
                        return
                    } else {
                        pairedVertices.add(neighbour)
                        saveStep(AugmentingStepType.Nothing("Vegyük be a párját a vizsgálandó csúcsok közé"))
                    }
                }
            }
            unpairedVertices.clear()
            for (vertex in pairedVertices) {
                vertex.visited = true
                if (vertex.pair?.visited == false) {
                    vertex.pair?.parent = vertex
                    unpairedVertices.add(vertex.pair!!)
                }
            }
            pairedVertices.clear()
        }
    }

    private fun getNextVertex(): AugmentingPathVertex? {
        saveStep(AugmentingStepType.Nothing("Keresünk egy párosítatlan csúcsot"))
        return vertices.firstOrNull { !it.visited && it.pair == null }
    }

    private fun reset() {
        for (vertex in vertices) {
            vertex.visited = false
            vertex.parent = null
        }
    }

    private fun augmentFromVertex(vertex: AugmentingPathVertex) {
        var current: AugmentingPathVertex? = vertex
        while (current != null) {
            val parent = current.parent
            if (parent != null) {
                val grandParent = parent.parent
                parent.pair = current
                current.pair = parent
                current = grandParent
            } else {
                break
            }
        }
    }

    fun toGraphicalGraph(stepType: AugmentingStepType = AugmentingStepType.Nothing()): GraphicalGraph {
        val graphicalVertices = vertices.map { vertex ->
            val coordinates = getVertexCoordinates(vertex)
            GraphicalVertex(
                x = coordinates.first,
                y = coordinates.second,
                label = vertex.id,
                selected = vertex.visited,
                highlight = if (vertex in unpairedVertices || vertex in pairedVertices) ORANGE else Color.Transparent,
                highlightType = HighlightType.CIRCLE,
            )
        }

        val graphicalEdges = mutableListOf<GraphicalEdge>()

        vertices.forEach { vertex ->
            vertex.neighbours.forEach { neighbour ->
                if (vertex.id < neighbour.id) {
                    graphicalEdges.add(
                        GraphicalEdge(
                            startGraphicalVertex = graphicalVertices.first { it.label == vertex.id },
                            endGraphicalVertex = graphicalVertices.first { it.label == neighbour.id },
                            color = if (vertex.parent == neighbour || neighbour.parent == vertex) DARK_GREEN else Color.Black,
                            selected = vertex.pair == neighbour,
                        )
                    )
                }
            }
        }

        return GraphicalGraph(
            graphicalVertices = graphicalVertices,
            graphicalEdges = graphicalEdges,
            stepType = stepType
        )
    }
}

fun Graph.toAugmentingPathGraph(): AugmentingPathGraph {
    val augmentingPathVertices = vertices.map { vertex ->
        AugmentingPathVertex(
            id = vertex.id,
            neighbours = mutableSetOf()
        )
    }.toMutableSet()
    for (edge in edges) {
        val fromVertex = augmentingPathVertices.firstOrNull { it.id == edge.fromVertex.id }
        val toVertex = augmentingPathVertices.firstOrNull { it.id == edge.toVertex.id }
        if (fromVertex != null && toVertex != null) {
            fromVertex.neighbours.add(toVertex)
            toVertex.neighbours.add(fromVertex)
        }
    }
    return AugmentingPathGraph(
        vertices = augmentingPathVertices,
        name = name,
        idCoordinatesMap = idCoordinatesMap,
    )
}