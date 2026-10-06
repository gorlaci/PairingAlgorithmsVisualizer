package hu.gorlaci.pairingalgorithmsvisualizer.model.dfs

import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.AlgorithmRunningGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.quiz.DepthFirstSearchStepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.quiz.NeighbourStatus
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.GRAY
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_BLUE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.RED
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.HighlightType

class DepthFirstSearchGraph(
    override val vertices: MutableSet<DepthFirstSearchVertex> = mutableSetOf(),
    name: String = "",
    idCoordinatesMap: MutableMap<String, Pair<Double, Double>> = mutableMapOf(),
) : Graph<DepthFirstSearchVertex, Edge<DepthFirstSearchVertex>>(
    name = name,
    vertices = vertices,
    edges = mutableSetOf(),
    idCoordinatesMap = idCoordinatesMap,
    newEdge = { from, to -> Edge(from, to) },
),
    AlgorithmRunningGraph {
    private constructor(
        vertices: MutableSet<DepthFirstSearchVertex> = mutableSetOf(),
        name: String = "",
        idCoordinatesMap: MutableMap<String, Pair<Double, Double>> = mutableMapOf(),
        activeVertex: DepthFirstSearchVertex? = null,
        activeNeighbour: DepthFirstSearchVertex? = null,
        startingVertex: DepthFirstSearchVertex? = null,
    ) : this(
        vertices = vertices,
        name = name,
        idCoordinatesMap = idCoordinatesMap,
    ) {
        this.activeVertex = activeVertex
        this.activeNeighbour = activeNeighbour
        this.startingVertex = startingVertex
    }

    override val edges: MutableSet<Edge<DepthFirstSearchVertex>>
        get() {
            val set = mutableSetOf<Edge<DepthFirstSearchVertex>>()
            vertices.forEach { vertex ->
                vertex.neighbours.forEach { neighbour ->
                    if (vertex.name < neighbour.name) {
                        set.add(Edge(vertex, neighbour))
                    }
                }
            }
            return set
        }

    var activeVertex: DepthFirstSearchVertex? = null
    var activeNeighbour: DepthFirstSearchVertex? = null

    fun copy(): DepthFirstSearchGraph {
        val newVertices = vertices.map { vertex ->
            DepthFirstSearchVertex(
                id = vertex.id,
                row = vertex.row,
                reachedNumber = vertex.reachedNumber,
                finishedNumber = vertex.finishedNumber,
            )
        }.toMutableSet()

        val newActiveVertex = newVertices.find { it.id == activeVertex?.id }
        val newActiveNeighbour = newVertices.find { it.id == activeNeighbour?.id }
        val newStartingVertex = newVertices.find { it.id == startingVertex?.id }

        newVertices.forEach { newVertex ->
            val originalVertex = vertices.first { it.id == newVertex.id }
            originalVertex.neighbours.forEach { neighbour ->
                val newNeighbour = newVertices.first { it.id == neighbour.id }
                newVertex.neighbours.add(newNeighbour)
            }
            newVertex.parent = newVertices.find { it.id == originalVertex.parent?.id }
        }

        return DepthFirstSearchGraph(
            vertices = newVertices,
            name = name,
            idCoordinatesMap = idCoordinatesMap.toMutableMap(),
            activeVertex = newActiveVertex,
            activeNeighbour = newActiveNeighbour,
            startingVertex = newStartingVertex,
        )
    }

    val steps = mutableListOf<Triple<DepthFirstSearchGraph, StepType, DepthFirstSearchGraph>>()

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

    fun getTree(): DepthFirstSearchGraph {
        var newActiveNeighbour: DepthFirstSearchVertex? = null
        var newActiveVertex: DepthFirstSearchVertex? = null
        val treeVertices = vertices.filter { it.reachedNumber != null }.map { vertex ->
            DepthFirstSearchVertex(
                id = vertex.id,
                row = vertex.row,
                reachedNumber = vertex.reachedNumber,
                finishedNumber = vertex.finishedNumber,
            )
        }.toMutableSet()
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
            if (originalVertex == activeVertex) {
                newActiveVertex = newVertex
            }
        }
        return DepthFirstSearchGraph(
            vertices = treeVertices,
            activeVertex = newActiveVertex,
            activeNeighbour = newActiveNeighbour,
        )
    }

    private val treeGrid = mutableListOf<MutableList<DepthFirstSearchVertex>>()

    private fun addToTreeGrid(vertex: DepthFirstSearchVertex) {
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

    private var startingVertex: DepthFirstSearchVertex? = null

    private var reached = 0
    private var finished = 0

    private fun expand(vertex: DepthFirstSearchVertex) {
        activeVertex = vertex
        vertex.reachedNumber = ++reached
        saveStep("Vizsgáljuk a ${vertex.name} csúcsot")
        for (neighbour in vertex.neighbours.sortedBy { it.name }) {
            activeNeighbour = neighbour
            if (neighbour.reachedNumber == null) {
                neighbour.parent = vertex
                neighbour.row = vertex.row + 1
                addToTreeGrid(neighbour)
                saveStep(
                    DepthFirstSearchStepType.SelectedNeighbour(
                        description =
                            "A ${vertex.name} csúcs szomszédja a ${neighbour.name} csúcs, amit még nem vizsgáltunk, így bejárjuk",
                        vertex = vertex,
                        neighbour = neighbour,
                        neighbourStatus = NeighbourStatus.VISIT,
                    ),
                )
                activeNeighbour = null
                expand(neighbour)
                activeVertex = vertex
            } else {
                saveStep(
                    DepthFirstSearchStepType.SelectedNeighbour(
                        description =
                            "A ${vertex.name} csúcs szomszédja a ${neighbour.name} csúcs, amit már vizsgáltunk, így nem járjuk be újra",
                        vertex = vertex,
                        neighbour = neighbour,
                        neighbourStatus = NeighbourStatus.SKIP,
                    ),
                )
            }
        }
        activeNeighbour = null
        vertex.finishedNumber = ++finished
        saveStep(
            "A ${vertex.name} csúcs összes szomszédját megvizsgáltuk, így visszalépünk",
        )
        activeVertex = null
    }

    fun runAlgorithm(startingVertex: DepthFirstSearchVertex? = null) {
        this.startingVertex = startingVertex

        saveStep()

        if (startingVertex != null) {
            startingVertex.row = 0
            activeVertex = startingVertex
            addToTreeGrid(startingVertex)
            saveStep("Induljunk el a ${startingVertex.name} csúcsból")
        }

        while (finished < vertices.size) {
            if (activeVertex == null) {
                activeVertex =
                    vertices.filter { it.reachedNumber == null }.minByOrNull { it.name }!!
                activeVertex?.let {
                    it.row = 0
                    addToTreeGrid(it)
                }
                saveStep(
                    DepthFirstSearchStepType.SelectedVertex(
                        description =
                            "A vizsgálandó csúcsok listája üres, járjuk be a komponenst a ${activeVertex?.name} csúcsból",
                        vertex = activeVertex!!,
                    ),
                )
            }
            expand(activeVertex!!)
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
                highlight = when {
                    vertex == activeVertex -> LIGHT_ORANGE
                    vertex == activeNeighbour -> LIGHT_BLUE
                    vertex.finishedNumber != null -> Color.Black
                    vertex.reachedNumber != null -> GRAY
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
        activeVertex = null
        steps.clear()
        treeGrid.clear()
        startingVertex = null
        activeNeighbour = null
        finished = 0
        reached = 0

        vertices.forEach { vertex ->
            vertex.row = -1
            vertex.parent = null
            vertex.reachedNumber = null
            vertex.finishedNumber = null
        }
    }
}

fun Graph<out Vertex, out Edge<out Vertex>>.toDepthFirstSearchGraph(): DepthFirstSearchGraph {
    val dfsVertices = vertices.map { vertex ->
        DepthFirstSearchVertex(
            id = vertex.id,
        )
    }.toMutableSet()

    edges.forEach { edge ->
        val fromVertex = dfsVertices.find { it.id == edge.fromVertex.id }
        val toVertex = dfsVertices.find { it.id == edge.toVertex.id }
        if (fromVertex != null && toVertex != null) {
            fromVertex.neighbours.add(toVertex)
            toVertex.neighbours.add(fromVertex)
        }
    }

    return DepthFirstSearchGraph(
        vertices = dfsVertices,
        name = name,
        idCoordinatesMap = idCoordinatesMap.toMutableMap(),
    )
}
