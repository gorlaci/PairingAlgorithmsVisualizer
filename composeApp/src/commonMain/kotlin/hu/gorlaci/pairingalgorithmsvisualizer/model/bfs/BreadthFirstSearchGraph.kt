package hu.gorlaci.pairingalgorithmsvisualizer.model.bfs

import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.GRAY
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_BLUE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.RED
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.HighlightType

class BreadthFirstSearchGraph(
    override val vertices: MutableSet<BreadthFirstSearchVertex> = mutableSetOf(),
    name: String = "",
    idCoordinatesMap: MutableMap<String, Pair<Double, Double>> = mutableMapOf(),
) : Graph<BreadthFirstSearchVertex, Edge<BreadthFirstSearchVertex>>(
    name = name,
    vertices = vertices,
    edges = mutableSetOf(),
    idCoordinatesMap = idCoordinatesMap,
    newEdge = { from, to -> Edge(from, to) },
) {
    private constructor(
        vertices: MutableSet<BreadthFirstSearchVertex> = mutableSetOf(),
        name: String = "",
        idCoordinatesMap: MutableMap<String, Pair<Double, Double>> = mutableMapOf(),
        queue: MutableList<BreadthFirstSearchVertex> = mutableListOf(),
        activeVertex: BreadthFirstSearchVertex? = null,
        activeNeighbour: BreadthFirstSearchVertex? = null,
        processedVertices: MutableSet<BreadthFirstSearchVertex> = mutableSetOf(),
        startingVertex: BreadthFirstSearchVertex? = null,
    ) : this(
        vertices = vertices,
        name = name,
        idCoordinatesMap = idCoordinatesMap,
    ) {
        this.queue.addAll(queue)
        this.processedVertices.addAll(processedVertices)
        this.activeVertex = activeVertex
        this.activeNeighbour = activeNeighbour
        this.startingVertex = startingVertex
    }

    override val edges: MutableSet<Edge<BreadthFirstSearchVertex>>
        get() {
            val set = mutableSetOf<Edge<BreadthFirstSearchVertex>>()
            vertices.forEach { vertex ->
                vertex.neighbours.forEach { neighbour ->
                    if (vertex.name < neighbour.name) {
                        set.add(Edge(vertex, neighbour))
                    }
                }
            }
            return set
        }

    val queue = mutableListOf<BreadthFirstSearchVertex>()
    var activeVertex: BreadthFirstSearchVertex? = null
    var activeNeighbour: BreadthFirstSearchVertex? = null
    val processedVertices: MutableSet<BreadthFirstSearchVertex> = mutableSetOf()

    fun copy(): BreadthFirstSearchGraph {
        val newVertices = vertices.map { vertex ->
            BreadthFirstSearchVertex(
                id = vertex.id,
                distance = vertex.distance,
            )
        }.toMutableSet()

        val newQueue = queue.map { vertex ->
            newVertices.first { it.id == vertex.id }
        }.toMutableList()

        val newActiveVertex = newVertices.find { it.id == activeVertex?.id }
        val newActiveNeighbour = newVertices.find { it.id == activeNeighbour?.id }
        val newStartingVertex = newVertices.find { it.id == startingVertex?.id }
        val newProcessedVertices = processedVertices.map { vertex ->
            newVertices.first { it.id == vertex.id }
        }.toMutableSet()

        newVertices.forEach { newVertex ->
            val originalVertex = vertices.first { it.id == newVertex.id }
            originalVertex.neighbours.forEach { neighbour ->
                val newNeighbour = newVertices.first { it.id == neighbour.id }
                newVertex.neighbours.add(newNeighbour)
            }
            newVertex.parent = newVertices.find { it.id == originalVertex.parent?.id }
        }

        return BreadthFirstSearchGraph(
            vertices = newVertices,
            name = name,
            idCoordinatesMap = idCoordinatesMap.toMutableMap(),
            queue = newQueue,
            activeVertex = newActiveVertex,
            activeNeighbour = newActiveNeighbour,
            processedVertices = newProcessedVertices,
            startingVertex = newStartingVertex,
        )
    }

    val steps = mutableListOf<Triple<BreadthFirstSearchGraph, StepType, BreadthFirstSearchGraph>>()

    private fun saveStep(stepType: StepType = StepType.Nothing()) {
        steps.add(
            Triple(
                copy(),
                stepType,
                getTree(),
            ),
        )
    }

    private fun saveStep(description: String) {
        saveStep(StepType.Nothing(description))
    }

    fun getTree(): BreadthFirstSearchGraph {
        val newProcessedVertices = processedVertices.map { vertex ->
            BreadthFirstSearchVertex(
                id = vertex.id,
                distance = vertex.distance,
            )
        }
        val newQueue = queue.map { vertex ->
            BreadthFirstSearchVertex(
                id = vertex.id,
                distance = vertex.distance,
            )
        }
        val newActiveVertex = activeVertex?.let { vertex ->
            BreadthFirstSearchVertex(
                id = vertex.id,
                distance = vertex.distance,
            )
        }
        var newActiveNeighbour: BreadthFirstSearchVertex? = null
        val treeVertices = (newProcessedVertices + newQueue).toMutableSet()
        newActiveVertex?.let { treeVertices.add(it) }
        treeVertices.forEach { newVertex ->
            val originalVertex = vertices.first { it.id == newVertex.id }
            originalVertex.parent?.let { parent ->
                val newParent = treeVertices.first { it.id == parent.id }
                newVertex.parent = newParent
                newVertex.neighbours.add(newParent)
                newParent.neighbours.add(newVertex)
            }
            if (originalVertex == activeNeighbour) {
                newActiveNeighbour = newVertex
            }
        }
        return BreadthFirstSearchGraph(
            vertices = treeVertices,
            activeVertex = newActiveVertex,
            queue = newQueue.toMutableList(),
            processedVertices = newProcessedVertices.toMutableSet(),
            activeNeighbour = newActiveNeighbour,
        )
    }

    private val treeGrid = mutableListOf<MutableList<BreadthFirstSearchVertex>>()

    private fun addToTreeGrid(vertex: BreadthFirstSearchVertex) {
        val rowIndex = vertex.row
        while (treeGrid.size <= rowIndex) {
            treeGrid.add(mutableListOf())
        }
        treeGrid[rowIndex].add(vertex)
    }

    private fun calculateTreeCoordinates(
        screenWidth: Double = 400.0,
        screenHeight: Double = 500.0,
    ): MutableMap<String, Pair<Double, Double>> {
        if (treeGrid.last().isEmpty()) {
            treeGrid.removeLast()
        }

        val rows = treeGrid.size
        val cols = treeGrid.maxOfOrNull { it.size } ?: return mutableMapOf()

        val rowDiff = minOf(screenHeight / (rows - 1), 100.0)
        val colDiff = minOf(screenWidth / (cols - 1), 100.0)

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

    private fun saveTreeCoordinates(
        screenWidth: Double = 400.0,
        screenHeight: Double = 500.0,
    ) {
        val coordinates = calculateTreeCoordinates(screenWidth, screenHeight)
        for (tree in steps.map { it.third }) {
            if (tree.idCoordinatesMap.isEmpty()) {
                tree.idCoordinatesMap.putAll(coordinates)
            }
        }
    }

    private var startingVertex: BreadthFirstSearchVertex? = null

    fun runAlgorithm(startingVertex: BreadthFirstSearchVertex? = null) {
        this.startingVertex = startingVertex

        saveStep()

        if (startingVertex != null) {
            startingVertex.distance = 0
            startingVertex.row = 0
            addToTreeGrid(startingVertex)
            saveStep("Induljunk el a ${startingVertex.name} csúcsból")
        }

        while (processedVertices.size < vertices.size) {
            if (queue.isEmpty()) {
                activeVertex =
                    vertices.filter { it !in processedVertices }.minByOrNull { it.name }!!
                activeVertex?.let {
                    it.row = 0
                    addToTreeGrid(it)
                }
                saveStep(
                    "A vizsgálandó csúcsok listája üres, járjuk be a komponenst ${activeVertex?.name} csúcsból",
                )
            } else {
                activeVertex = queue.removeFirst()
                saveStep("A listában a következő csúcs: ${activeVertex?.name}")
            }
            saveStep(
                "Vegyük be a vizsgálandó csúcsok közé a kiválasztott csúcs még nem vizsgált szomszédait",
            )
            activeVertex?.let { vertex ->
                vertex.neighbours.sortedBy { it.name }.forEach { neighbour ->
                    activeNeighbour = neighbour
                    if (neighbour !in processedVertices && neighbour !in queue) {
                        neighbour.distance = vertex.distance?.plus(1)
                        neighbour.row = vertex.row + 1
                        neighbour.parent = vertex
                        queue.add(neighbour)
                        addToTreeGrid(neighbour)
                    }
                    saveStep(
                        "Vegyük be a vizsgálandó csúcsok közé a kiválasztott csúcs még nem vizsgált szomszédait",
                    )
                }
                activeNeighbour = null
                saveStep("Megvizsgáltuk a csúcs összes szomszédját")

                processedVertices.add(vertex)
                activeVertex = null

                saveStep("Megvizsgáltuk a csúcs összes szomszédját")
            }
        }

        saveStep("Feldolgoztuk az összes csúcsot, a bejárás véget ért")
        saveStep()
        saveTreeCoordinates()
    }

    override fun toGraphicalGraph(stepType: StepType): GraphicalGraph {
        val graphicalVertices = vertices.map { vertex ->
            val coordinates = getVertexCoordinates(vertex)
            GraphicalVertex(
                x = coordinates.first,
                y = coordinates.second,
                name = vertex.name,
                highlight = when (vertex) {
                    activeVertex -> LIGHT_ORANGE
                    activeNeighbour -> LIGHT_BLUE
                    in queue -> GRAY
                    in processedVertices -> Color.Black
                    else -> Color.Transparent
                },
                highlightType = HighlightType.CIRCLE,
                innerColor = if (vertex == startingVertex) {
                    LIGHT_ORANGE
                } else {
                    Color.White
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
                                it.name == vertex.name
                            },
                            endGraphicalVertex = graphicalVertices.first {
                                it.name == neighbour.name
                            },
                            selected = vertex.parent == neighbour || neighbour.parent == vertex,
                            color = if (vertex.parent == neighbour || neighbour.parent == vertex) {
                                RED
                            } else {
                                Color.Black
                            },
                            highlight = if (
                                (vertex == activeVertex && neighbour == activeNeighbour) ||
                                (vertex == activeNeighbour && neighbour == activeVertex)
                            ) {
                                LIGHT_BLUE
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

    override fun resetAlgorithm() {
        queue.clear()
        activeVertex = null
        processedVertices.clear()
        steps.clear()
        treeGrid.clear()
        startingVertex = null
        activeNeighbour = null

        vertices.forEach { vertex ->
            vertex.distance = null
            vertex.row = -1
            vertex.parent = null
        }
    }
}

fun Graph<out Vertex, out Edge<out Vertex>>.toBreadthFirstSearchGraph(): BreadthFirstSearchGraph {
    val bfsVertices = vertices.map { vertex ->
        BreadthFirstSearchVertex(
            id = vertex.id,
        )
    }.toMutableSet()

    edges.forEach { edge ->
        val fromVertex = bfsVertices.find { it.id == edge.fromVertex.id }
        val toVertex = bfsVertices.find { it.id == edge.toVertex.id }
        if (fromVertex != null && toVertex != null) {
            fromVertex.neighbours.add(toVertex)
            toVertex.neighbours.add(fromVertex)
        }
    }

    return BreadthFirstSearchGraph(
        vertices = bfsVertices,
        name = name,
        idCoordinatesMap = idCoordinatesMap.toMutableMap(),
    )
}
