package hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath

import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.quiz.AugmentingStepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.HighlightType

class AugmentingPathGraph(
    override val vertices: MutableSet<AugmentingPathVertex> = mutableSetOf(),
    name: String = "",
    idCoordinatesMap: MutableMap<Char, Pair<Double, Double>> = mutableMapOf(),
) : Graph<AugmentingPathVertex, Edge>(
    name = name,
    vertices = vertices,
    edges = mutableSetOf(),
    idCoordinatesMap = idCoordinatesMap,
    newVertex = { AugmentingPathVertex(it) },
    newEdge = { from, to -> Edge(from, to) },
) {

    override val edges: MutableSet<Edge>
        get() {
            val set = mutableSetOf<Edge>()
            vertices.forEach { vertex ->
                vertex.neighbours.forEach { neighbour ->
                    if (vertex.id < neighbour.id) {
                        set.add(Edge(vertex, neighbour))
                    }
                }
            }
            return set
        }


    private val unpairedVertices = mutableSetOf<AugmentingPathVertex>()
    private val pairedVertices = mutableSetOf<AugmentingPathVertex>()

    private val class1 = mutableSetOf<AugmentingPathVertex>()
    private val class2 = mutableSetOf<AugmentingPathVertex>()

    private var activeVertex: AugmentingPathVertex? = null
    private val augmentingPathVertices = mutableSetOf<AugmentingPathVertex>()

    private val minCoverSet = mutableSetOf<AugmentingPathVertex>()

    private constructor(
        vertices: MutableSet<AugmentingPathVertex>,
        unpairedVertices: MutableSet<AugmentingPathVertex>,
        pairedVertices: MutableSet<AugmentingPathVertex>,
        class1: MutableSet<AugmentingPathVertex>,
        class2: MutableSet<AugmentingPathVertex>,
        activeVertex: AugmentingPathVertex?,
        newAugmentingPathVertices: MutableSet<AugmentingPathVertex>,
        minCoverSet: MutableSet<AugmentingPathVertex>,
    ) : this(vertices = vertices) {
        this.unpairedVertices.addAll(unpairedVertices)
        this.pairedVertices.addAll(pairedVertices)
        this.class1.addAll(class1)
        this.class2.addAll(class2)
        this.activeVertex = activeVertex
        this.augmentingPathVertices.addAll(newAugmentingPathVertices)
        this.minCoverSet.addAll(minCoverSet)
    }


    val steps = mutableListOf<Pair<GraphicalGraph, AugmentingPathGraph>>()

    private fun saveStep(stepType: AugmentingStepType = AugmentingStepType.Nothing()) {
        steps.add(toGraphicalGraph(stepType) to getTree())
    }


    private var augmentMade = true

    fun getTree(): AugmentingPathGraph {
        val treeVertices = vertices.filter { it.visited || it in unpairedVertices || it in pairedVertices }
        val newTreeVertices = treeVertices.map {
            AugmentingPathVertex(
                id = it.id,
                visited = it.visited,
            )
        }.toMutableSet()

        val newUnpairedVertices = mutableSetOf<AugmentingPathVertex>()
        val newPairedVertices = mutableSetOf<AugmentingPathVertex>()
        val newClass1 = mutableSetOf<AugmentingPathVertex>()
        val newClass2 = mutableSetOf<AugmentingPathVertex>()
        var newActiveVertex: AugmentingPathVertex? = null
        val newAugmentingPathVertices = mutableSetOf<AugmentingPathVertex>()
        val newMinCoverSet = mutableSetOf<AugmentingPathVertex>()

        treeVertices.forEach { vertex ->
            val newVertex = newTreeVertices.first { it.id == vertex.id }
            if (vertex.pair != null) {
                val newPair = newTreeVertices.find { it.id == vertex.pair!!.id }
                newVertex.pair = newPair
            }
            if (vertex.parent != null) {
                val newParent = newTreeVertices.find { it.id == vertex.parent!!.id }
                newVertex.parent = newParent
                if (newParent != null) {
                    newVertex.neighbours.add(newParent)
                    newParent.neighbours.add(newVertex)
                }
            }

            if (vertex in unpairedVertices) {
                newUnpairedVertices.add(newVertex)
            }
            if (vertex in pairedVertices) {
                newPairedVertices.add(newVertex)
            }
            if (vertex in class1) {
                newClass1.add(newVertex)
            }
            if (vertex in class2) {
                newClass2.add(newVertex)
            }
            if (vertex == activeVertex) {
                newActiveVertex = newVertex
            }
            if (vertex in augmentingPathVertices) {
                newAugmentingPathVertices.add(newVertex)
            }
        }

        val graph = AugmentingPathGraph(
            vertices = newTreeVertices,
            unpairedVertices = newUnpairedVertices,
            pairedVertices = newPairedVertices,
            class1 = newClass1,
            class2 = newClass2,
            activeVertex = newActiveVertex,
            newAugmentingPathVertices = newAugmentingPathVertices,
            minCoverSet = newMinCoverSet,
        )

        return graph
    }

    private val treeGrid = mutableListOf<MutableList<AugmentingPathVertex>>()

    private fun calculateTreeCoordinates(): MutableMap<Char, Pair<Double, Double>> {
        if (treeGrid.last().isEmpty()) {
            treeGrid.removeLast()
        }

        val rows = treeGrid.size
        val cols = treeGrid.maxOfOrNull { it.size } ?: return mutableMapOf()

        val rowDiff = minOf(500.0 / (rows - 1), 100.0)
        val colDiff = minOf(400.0 / (cols - 1), 100.0)

        val coordinates = mutableMapOf<Char, Pair<Double, Double>>()

        var y = (rowDiff * (rows - 1)) / 2

        treeGrid.forEach { row ->
            var x = -(colDiff * (cols - 1)) / 2
            row.forEach { vertex ->
                coordinates[vertex.id[0]] = Pair(x, y)
                x += colDiff
            }
            y -= rowDiff
        }
        return coordinates
    }

    private fun saveTreeCoordinates() {
        val coordinates = calculateTreeCoordinates()
        for (tree in steps.map { it.second }) {
            if (tree.idCoordinatesMap.isEmpty()) {
                tree.idCoordinatesMap.putAll(coordinates)
            }
        }
    }

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
        saveStep(AugmentingStepType.Nothing("Nincs már javító út, kész a maximális párosítás"))

        saveStep()

        saveStep(AugmentingStepType.Nothing("Keressünk egy minimális lefogó ponthalmazt!"))
        findAugmentingPath(saveSteps = false)
        saveStep(AugmentingStepType.Nothing("Vizsgáljuk az utolsó fát!"))
        markMinCoverSet()
        saveStep(AugmentingStepType.Nothing("A megtalált párosítás és a megtalált lefogó ponthalmaz mérete megegyezik, így mindkettő optimális."))
        saveStep()

    }

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


    fun findAugmentingPath(saveSteps: Boolean = true) {

        augmentMade = false

        if (saveSteps) {
            saveStep(AugmentingStepType.Nothing("Keressünk javítóutat a gráfban!"))
        }

        unpairedVertices.addAll(class1.filter { it.pair == null && !it.visited })

        treeGrid.add(mutableListOf())
        treeGrid.last().addAll(unpairedVertices)
        if (saveSteps) {
            saveStep(AugmentingStepType.Nothing("Elindulunk ez egyik osztálybeli összes párosítatlan csúcsból"))
        }
        pairedVertices.clear()

        while (unpairedVertices.isNotEmpty()) {
            val unpairedCopy = unpairedVertices.toSet()

            treeGrid.add(mutableListOf())

            for (vertex in unpairedCopy) {
                vertex.visited = true
                activeVertex = vertex
                if (saveSteps) {
                    saveStep(AugmentingStepType.Nothing("Vizsgáljuk ${vertex.id} csúcsot"))
                }
                for (neighbour in vertex.neighbours.filter { !it.visited }) {
                    neighbour.parent = vertex
                    neighbour.visited = true
                    pairedVertices.add(neighbour)

                    treeGrid.last().add(neighbour)
                }
                if (saveSteps) {
                    saveStep(AugmentingStepType.Nothing("Vegyük be a szomszédait a vizsgálandó csúcsok közé"))
                }
                unpairedVertices.remove(vertex)
                activeVertex = null
            }

            treeGrid.add(mutableListOf())

            unpairedVertices.clear()
            val pairedCopy = pairedVertices.toSet()
            for (vertex in pairedCopy) {
                activeVertex = vertex
                if (saveSteps) {
                    saveStep(AugmentingStepType.Nothing("Vizsgáljuk ${vertex.id} csúcsot"))
                }
                if (vertex.pair == null) {
                    if (saveSteps) {
                        saveStep(AugmentingStepType.Nothing("Találtunk egy párosítatlan csúcsot"))
                    }
                    markAugmentingPath(vertex)
                    if (saveSteps) {
                        saveStep(AugmentingStepType.Nothing("Javítsunk a javítóút mentén!"))
                    }
                    augmentFromVertex(vertex)
                    if (saveSteps) {
                        saveStep(AugmentingStepType.Nothing("Javítottunk a párosításon"))
                    }
                    saveTreeCoordinates()
                    augmentMade = true
                    reset()
                    return
                }
                unpairedVertices.add(vertex.pair!!)
                vertex.pair!!.parent = vertex
                treeGrid.last().add(vertex.pair!!)
                if (saveSteps) {
                    saveStep(AugmentingStepType.Nothing("Vegyük be a párját a vizsgálandó csúcsok közé"))
                }
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

        treeGrid.clear()
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

    private fun markMinCoverSet() {
        minCoverSet.addAll(class1.filter { !it.visited })
        saveStep(AugmentingStepType.Nothing("Vegyük a fa által nem fedett kék csúcsokat"))
        minCoverSet.addAll(class2.filter { it.visited })
        saveStep(AugmentingStepType.Nothing("És a fa által fedett piros csúcsokat"))
        saveStep(AugmentingStepType.Nothing("Ezzel megkaptuk a minimális lefogó ponthalmazt"))
    }

    override fun toGraphicalGraph(stepType: StepType): GraphicalGraph {
        val graphicalVertices = vertices.map { vertex ->
            val coordinates = getVertexCoordinates(vertex)
            GraphicalVertex(
                x = coordinates.first,
                y = coordinates.second,
                label = vertex.id,
                highlight = when {
                    vertex in minCoverSet -> DARK_GREEN

                    vertex == activeVertex -> LIGHT_ORANGE

                    vertex in unpairedVertices -> RED

                    vertex in pairedVertices -> BLUE

                    vertex.visited -> GRAY

                    else -> Color.Transparent
                },
                highlightType = HighlightType.CIRCLE,
                innerColor = if (vertex in class1) LIGHT_BLUE else if (vertex in class2) LIGHT_RED else Color.White
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
                            highlight = if (vertex in augmentingPathVertices && neighbour in augmentingPathVertices) {
                                LIGHT_YELLOW
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

fun Graph<out Vertex, out Edge>.toAugmentingPathGraph(): AugmentingPathGraph {
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