package hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath

import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.quiz.AugmentingStepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
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
//        println("Saved step: ${stepType.description}")
    }

    private val unpairedVertices = mutableSetOf<AugmentingPathVertex>()
    private val pairedVertices = mutableSetOf<AugmentingPathVertex>()

    private var augmentMade = true

    fun runAlgorithm() {
        saveStep()
        createClasses()
        saveStep(AugmentingStepType.Nothing("Megállapítjuk a két osztályt"))
        reset()
        saveStep(AugmentingStepType.Nothing("Kiindulunk az üres párosításból"))
        while (augmentMade) {
            findAugmentingPath()
        }
        reset()
        saveStep(AugmentingStepType.Nothing("Megvizsgáltunk minden párosítatlan csúcsot, kész a maximális párosítás"))
    }

    val class1 = mutableSetOf<AugmentingPathVertex>()
    val class2 = mutableSetOf<AugmentingPathVertex>()

    private fun createClasses() {
        val unvisited = vertices.toMutableSet()

        while (unvisited.isNotEmpty()) {
            val vertex = unvisited.first()
            unvisited.remove(vertex)
            createClassesRecursive(vertex, class1, class2, unvisited)
        }
    }

    private fun createClassesRecursive(
        vertex: AugmentingPathVertex,
        currentClass: MutableSet<AugmentingPathVertex>,
        otherClass: MutableSet<AugmentingPathVertex>,
        unvisited: MutableSet<AugmentingPathVertex>
    ) {
        currentClass.add(vertex)
        for (neighbour in vertex.neighbours) {
            if (neighbour in unvisited) {
                unvisited.remove(neighbour)
                createClassesRecursive(neighbour, otherClass, currentClass, unvisited)
            }
        }
    }

    private var activeVertex: AugmentingPathVertex? = null
    private var augmentingPathVertices = mutableSetOf<AugmentingPathVertex>()

    fun findAugmentingPath() {

        augmentMade = false

        saveStep(AugmentingStepType.Nothing("Keressünk javítóutat a gráfban!"))

        unpairedVertices.addAll(class1.filter { it.pair == null && !it.visited })

        if (unpairedVertices.isEmpty()) {
            return
        }

        saveStep(AugmentingStepType.Nothing("Elindulunk ez egyik osztálybeli összes párosítatlan csúcsból"))

        pairedVertices.clear()

        while (unpairedVertices.isNotEmpty()) {
            val unpairedCopy = unpairedVertices.toSet()
            for (vertex in unpairedCopy) {
                vertex.visited = true
                activeVertex = vertex
                saveStep(AugmentingStepType.Nothing("Vizsgáljuk ${vertex.id} csúcsot"))
                for (neighbour in vertex.neighbours.filter { !it.visited }) {
                    neighbour.parent = vertex
                    neighbour.visited = true
                    pairedVertices.add(neighbour)
                }
                saveStep(AugmentingStepType.Nothing("Vegyük be a szomszédait a vizsgálandó csúcsok közé"))
                unpairedVertices.remove(vertex)
                activeVertex = null
            }
            unpairedVertices.clear()
            val pairedCopy = pairedVertices.toSet()
            for (vertex in pairedCopy) {
                activeVertex = vertex
                saveStep(AugmentingStepType.Nothing("Vizsgáljuk ${vertex.id} csúcsot"))
                if (vertex.pair == null) {
                    saveStep(AugmentingStepType.Nothing("Találtunk egy párosítatlan csúcsot"))
                    markAugmentingPath(vertex)
                    saveStep(AugmentingStepType.Nothing("Javítsunk a javítóút mentén!"))
                    augmentFromVertex(vertex)
                    saveStep(AugmentingStepType.Nothing("Javítottunk a párosításon"))
                    augmentMade = true
                    reset()
                    return
                }
                unpairedVertices.add(vertex.pair!!)
                vertex.pair!!.parent = vertex
                saveStep(AugmentingStepType.Nothing("Vegyük be a párját a vizsgálandó csúcsok közé"))
                pairedVertices.remove(vertex)
                activeVertex = null
            }
            pairedVertices.clear()
        }
    }

    private fun reset() {
        for (vertex in vertices) {
            vertex.visited = false
            vertex.parent = null
        }
        activeVertex = null
        augmentingPathVertices.clear()
        unpairedVertices.clear()
        pairedVertices.clear()
    }

    private fun markAugmentingPath(vertex: AugmentingPathVertex) {
        var current: AugmentingPathVertex? = vertex
        while (current != null) {
            augmentingPathVertices.add(current)
            current = current.parent
        }
    }

    private fun augmentFromVertex(vertex: AugmentingPathVertex) {
        var current: AugmentingPathVertex? = vertex
        while (current != null) {
            val parent = current.parent!!
            val grandParent = parent.parent
            parent.pair = current
            current.pair = parent
            current = grandParent
        }
    }

    fun toGraphicalGraph(stepType: AugmentingStepType = AugmentingStepType.Nothing()): GraphicalGraph {
        val graphicalVertices = vertices.map { vertex ->
            val coordinates = getVertexCoordinates(vertex)
            GraphicalVertex(
                x = coordinates.first,
                y = coordinates.second,
                label = vertex.id,
                highlight = if (vertex == activeVertex) {
                    ORANGE
                } else if (vertex in unpairedVertices) {
                    LIGHT_RED
                } else if (vertex in pairedVertices) {
                    LIGHT_BLUE
                } else if (vertex.visited) {
                    GRAY
                } else {
                    Color.Transparent
                },
                highlightType = HighlightType.CIRCLE,
                innerColor = if (vertex in class1) BLUE else if (vertex in class2) RED else Color.White
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
                            color = if (vertex.parent == neighbour || neighbour.parent == vertex) BRIGHT_GREEN else Color.Black,
                            selected = vertex.pair == neighbour,
                            highlight = if (vertex in augmentingPathVertices && neighbour in augmentingPathVertices) {
                                PURPLE
                            } else {
                                Color.Transparent
                            },
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