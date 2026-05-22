package hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath

import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.HighlightType

class AugmentingPathGraph(
    override val vertices: MutableSet<AugmentingPathVertex> = mutableSetOf(),
    name: String = "",
    idCoordinatesMap: MutableMap<String, Pair<Double, Double>> = mutableMapOf(),
) : BipartiteGraph<AugmentingPathVertex, Edge<AugmentingPathVertex>>(
    name = name,
    vertices = vertices,
    edges = mutableSetOf(),
    idCoordinatesMap = idCoordinatesMap,
    newVertex = { AugmentingPathVertex(it) },
    newEdge = { from, to -> Edge(from, to) },
) {

    override val edges: MutableSet<Edge<AugmentingPathVertex>>
        get() {
            val set = mutableSetOf<Edge<AugmentingPathVertex>>()
            vertices.forEach { vertex ->
                vertex.neighbours.forEach { neighbour ->
                    if (vertex.name < neighbour.name) {
                        set.add(Edge(vertex, neighbour))
                    }
                }
            }
            return set
        }

    private val unpairedVertices = mutableSetOf<AugmentingPathVertex>()
    private val pairedVertices = mutableSetOf<AugmentingPathVertex>()

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

    private fun saveStep(stepType: StepType = StepType.Nothing()) {
        steps.add(toGraphicalGraph(stepType) to getTree())
    }

    private var augmentMade = true

    fun getTree(): AugmentingPathGraph {
        val treeVertices =
            vertices.filter { it.visited || it in unpairedVertices || it in pairedVertices }
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

    private fun calculateTreeCoordinates(): MutableMap<String, Pair<Double, Double>> {
        if (treeGrid.last().isEmpty()) {
            treeGrid.removeLast()
        }

        val rows = treeGrid.size
        val cols = treeGrid.maxOfOrNull { it.size } ?: return mutableMapOf()

        val rowDiff = minOf(500.0 / (rows - 1), 100.0)
        val colDiff = minOf(400.0 / (cols - 1), 100.0)

        val coordinates = mutableMapOf<String, Pair<Double, Double>>()

        var y = (rowDiff * (rows - 1)) / 2

        treeGrid.forEach { row ->
            var x = -(colDiff * (cols - 1)) / 2
            row.forEach { vertex ->
                coordinates[vertex.name] = Pair(x, y)
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
        saveStep(StepType.Nothing("Megállapítjuk a két osztályt"))
        reset()
        saveStep(StepType.Nothing("Kiindulunk a megadott párosításból"))
        while (augmentMade) {
            findAugmentingPath()
        }
        reset(resetForest = false)
        saveStep(StepType.AlgorithmEnd("Nincs már javító út, kész a maximális párosítás"))

        saveStep()

        saveStep(StepType.Nothing("Keressünk egy minimális lefogó ponthalmazt!"))
        findAugmentingPath(saveSteps = false)
        saveStep(StepType.Nothing("Vizsgáljuk az utolsó fát!"))
        markMinCoverSet()
        saveTreeCoordinates()

        reset()

        val pairingSize = minCoverSet.size

        saveStep(
            StepType.Nothing(
                "Találtunk egy $pairingSize elemű párosítást és egy $pairingSize elemű lefogó ponthalmazt, tehát\n$pairingSize <= ν(G) <= τ(G) <= $pairingSize,\nazaz ν(G) = τ(G) = $pairingSize",
            ),
        )
        saveStep()
    }

    fun findAugmentingPath(saveSteps: Boolean = true) {
        augmentMade = false

        if (saveSteps) {
            saveStep(StepType.Nothing("Keressünk javítóutat a gráfban!"))
        }

        unpairedVertices.addAll(class1.filter { it.pair == null && !it.visited })

        treeGrid.add(mutableListOf())
        treeGrid.last().addAll(unpairedVertices)
        if (saveSteps) {
            saveStep(
                StepType.Nothing(
                    "Elindulunk ez egyik osztálybeli összes párosítatlan csúcsból",
                ),
            )
        }
        pairedVertices.clear()

        while (unpairedVertices.isNotEmpty()) {
            val unpairedCopy = unpairedVertices.toSet()

            treeGrid.add(mutableListOf())

            for (vertex in unpairedCopy) {
                vertex.visited = true
                activeVertex = vertex
                if (saveSteps) {
                    saveStep(StepType.Nothing("Vizsgáljuk ${vertex.id} csúcsot"))
                }
                for (neighbour in vertex.neighbours.filter { !it.visited }) {
                    neighbour.parent = vertex
                    neighbour.visited = true
                    pairedVertices.add(neighbour)

                    treeGrid.last().add(neighbour)
                }
                if (saveSteps) {
                    saveStep(
                        StepType.Nothing(
                            "Vegyük be a szomszédait a vizsgálandó csúcsok közé",
                        ),
                    )
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
                    saveStep(StepType.Nothing("Vizsgáljuk ${vertex.id} csúcsot"))
                }
                if (vertex.pair == null) {
                    if (saveSteps) {
                        saveStep(StepType.Nothing("Találtunk egy párosítatlan csúcsot"))
                    }
                    markAugmentingPath(vertex)
                    if (saveSteps) {
                        saveStep(StepType.SkipPoint("Javítsunk a javítóút mentén!"))
                    }
                    augmentFromVertex(vertex)
                    if (saveSteps) {
                        saveStep(StepType.Nothing("Javítottunk a párosításon"))
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
                    saveStep(
                        StepType.Nothing("Vegyük be a párját a vizsgálandó csúcsok közé"),
                    )
                }
                pairedVertices.remove(vertex)
                activeVertex = null
            }
            pairedVertices.clear()
        }
    }

    private fun reset(resetForest: Boolean = true) {
        if (resetForest) {
            for (vertex in vertices) {
                vertex.visited = false
                vertex.parent = null
                unpairedVertices.clear()
                pairedVertices.clear()
                treeGrid.clear()
            }
        }
        activeVertex = null
        augmentingPathVertices.clear()
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
        saveStep(StepType.Nothing("Vegyük a fa által nem fedett kék csúcsokat"))
        minCoverSet.addAll(class2.filter { it.visited })
        saveStep(StepType.Nothing("És a fa által fedett piros csúcsokat"))
        saveStep(StepType.Nothing("Ezzel megkaptuk a minimális lefogó ponthalmazt"))
    }

    override fun toGraphicalGraph(stepType: StepType): GraphicalGraph {
        val graphicalVertices = vertices.map { vertex ->
            val coordinates = getVertexCoordinates(vertex)
            GraphicalVertex(
                x = coordinates.first,
                y = coordinates.second,
                name = vertex.name,
                highlight = when {
                    vertex in minCoverSet -> DARK_GREEN
                    vertex == activeVertex -> LIGHT_ORANGE
                    vertex in unpairedVertices -> RED
                    vertex in pairedVertices -> BLUE
                    vertex.visited -> GRAY
                    else -> Color.Transparent
                },
                highlightType = HighlightType.CIRCLE,
                innerColor = when (vertex) {
                    in class1 -> LIGHT_BLUE
                    in class2 -> LIGHT_RED
                    else -> Color.White
                },
            )
        }

        val graphicalEdges = mutableListOf<GraphicalEdge>()

        vertices.forEach { vertex ->
            vertex.neighbours.forEach { neighbour ->
                if (vertex.name < neighbour.name) {
                    graphicalEdges.add(
                        GraphicalEdge(
                            startGraphicalVertex = graphicalVertices.first {
                                it.name ==
                                    vertex.name
                            },
                            endGraphicalVertex = graphicalVertices.first {
                                it.name ==
                                    neighbour.name
                            },
                            color = if (vertex.parent == neighbour ||
                                neighbour.parent == vertex
                            ) {
                                DARK_GREEN
                            } else {
                                Color.Black
                            },
                            selected = vertex.pair == neighbour,
                            highlight = if (vertex in augmentingPathVertices &&
                                neighbour in augmentingPathVertices
                            ) {
                                LIGHT_YELLOW
                            } else {
                                Color.Transparent
                            },
                        ),
                    )
                }
            }
        }

        return GraphicalGraph(
            graphicalVertices = graphicalVertices,
            graphicalEdges = graphicalEdges,
            stepType = stepType,
        )
    }

    override fun pairVertices(
        vertexA: Vertex,
        vertexB: Vertex,
    ) {
        (vertexA as AugmentingPathVertex).pair = vertexB as AugmentingPathVertex
        vertexB.pair = vertexA
    }

    override fun unPairVertices(
        vertexA: Vertex,
        vertexB: Vertex,
    ) {
        (vertexA as AugmentingPathVertex).pair = null
        (vertexB as AugmentingPathVertex).pair = null
    }

    override fun getPair(vertex: Vertex): AugmentingPathVertex? =
        (vertex as AugmentingPathVertex).pair
}

fun Graph<out Vertex, out Edge<out Vertex>>.toAugmentingPathGraph(): AugmentingPathGraph {
    val augmentingPathVertices = vertices.map { vertex ->
        AugmentingPathVertex(
            id = vertex.id,
            neighbours = mutableSetOf(),
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
