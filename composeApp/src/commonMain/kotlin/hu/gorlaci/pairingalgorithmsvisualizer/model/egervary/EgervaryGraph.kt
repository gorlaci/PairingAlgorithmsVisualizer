package hu.gorlaci.pairingalgorithmsvisualizer.model.egervary

import androidx.compose.ui.graphics.Color
import hu.gorlaci.pairingalgorithmsvisualizer.model.BipartiteGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex

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

    val steps = mutableListOf<GraphicalGraph>()

    private fun saveStep(stepType: StepType = StepType.Nothing()) {
        steps.add(toGraphicalGraph(stepType))
    }

    private fun saveStep(description: String) {
        saveStep(StepType.Nothing(description))
    }

    fun addEdge(
        fromVertex: EgervaryVertex,
        toVertex: EgervaryVertex,
        weight: Int = 1,
    ) {
        val edge = EgervaryEdge(fromVertex, toVertex, weight)
        fromVertex.edges.add(edge)
        toVertex.edges.add(edge)
        edges.add(edge)
    }

    fun addEdge(
        fromName: String,
        toName: String,
        weight: Int = 1,
    ) {
        val fromVertex = vertices.find { it.name == fromName } ?: return
        val toVertex = vertices.find { it.name == toName } ?: return
        addEdge(fromVertex, toVertex, weight)
    }

    fun runAlgorithm() {
        saveStep()

        init()
        findMaximumRedMatching()
        while (!isPairingComplete()) {
            adjustLabels()
            findMaximumRedMatching()
        }

        saveStep("Kész vagyunk, a párosítás teljes")

        drawRed = false

        val totalWeight = edges.filter { it.selected }.sumOf { it.weight }

        val totalLabels = vertices.sumOf { it.label }

        saveStep(
            StepType.AlgorithmEnd(
                "Találtunk egy $totalWeight összsúlyú párosítást és egy $totalLabels összegű címkézést",
            ),
        )

        saveStep()
    }

    private fun init() {
        createClasses()

        saveStep("Kiegészítjük a gráfot, hogy biztosan legyen benne teljes párosítás")

        completeGraph()

        saveStep()
        for (edge in edges) {
            edge.selected = false
        }

        saveStep("Beállítjuk a csúcsok kezdeti címkéit")

        for (vertex in class1) {
            vertex.label = vertex.edges.maxOfOrNull { it.weight } ?: 0
        }
        for (vertex in class2) {
            vertex.label = 0
        }

        drawRed = true
        saveStep()
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
        saveStep("Keressünk maximális élszámú párosítást a piros részgráfban")

        augmentMade = true
        while (augmentMade) {
            findAugmentingPath()
        }
        saveStep("Nincs már javítóút")
    }

    val unpairedVertices = mutableSetOf<EgervaryVertex>()
    val pairedVertices = mutableSetOf<EgervaryVertex>()
    val visitedVertices = mutableSetOf<EgervaryVertex>()
    var activeVertex: EgervaryVertex? = null

    private fun findAugmentingPath() {
        augmentMade = false

        unpairedVertices.clear()
        pairedVertices.clear()
        visitedVertices.clear()

        saveStep("Keressünk javítóutat a piros élek között")

        unpairedVertices.addAll(class1.filter { it.pair == null })

        saveStep("Kiindulunk az A-beli párosítatlan csúcsokból")

        while (unpairedVertices.isNotEmpty()) {
            val unpairedCopy = unpairedVertices.toSet()
            for (vertex in unpairedCopy) {
                visitedVertices.add(vertex)
                activeVertex = vertex
                saveStep("Vizsgáljuk meg a(z) ${vertex.name} csúcsot")
                for (edge in (vertex.redEdges)) {
                    val neighbour = edge.otherEnd(vertex) ?: continue
                    if (neighbour in visitedVertices) {
                        continue
                    }
                    neighbour.parentEdge = edge
                    visitedVertices.add(neighbour)
                    pairedVertices.add(neighbour)
                }
                saveStep("Vegyük be a szomszédait a vizsgálandó csúcsok közé")
                unpairedVertices.remove(vertex)
                activeVertex = null
            }

            val pairedCopy = pairedVertices.toSet()
            for (vertex in pairedCopy) {
                saveStep("Vizsgáljuk meg a(z) ${vertex.name} csúcsot")
                if (vertex.pair == null) {
                    saveStep("Találtunk egy párosítatlan csúcsot")
                    markAugmentingPath(vertex)
                    saveStep(StepType.SkipPoint("Javítsunk a javítóút mentén!"))
                    augmentFromVertex(vertex)
                    saveStep("Bővítettük a párosítást")
                    augmentMade = true
                    reset()
                    return
                }
                vertex.pair?.let { pair ->
                    unpairedVertices.add(pair)
                    pair.parentEdge = pair.edges.first { it.selected }
                    saveStep("Vegyük be a párját a vizsgálandó csúcsok közé")
                }
                pairedVertices.remove(vertex)
            }
        }
    }

    val augmentingPathEdges = mutableSetOf<EgervaryEdge>()

    private fun markAugmentingPath(vertex: EgervaryVertex) {
        var current = vertex
        while (current.parentEdge != null) {
            val parentEdge = current.parentEdge ?: break
            augmentingPathEdges.add(parentEdge)
            current = parentEdge.otherEnd(current) ?: break
        }
    }

    private fun augmentFromVertex(vertex: EgervaryVertex) {
        var current = vertex
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
        pairedVertices.clear()
        unpairedVertices.clear()
        visitedVertices.clear()

        augmentingPathEdges.clear()
    }

    private val filteredClass1 = mutableSetOf<EgervaryVertex>()
    private val filteredClass2 = mutableSetOf<EgervaryVertex>()

    private val deltaEdges = mutableSetOf<EgervaryEdge>()

    private val setU = mutableSetOf<EgervaryVertex>()
    private val setT = mutableSetOf<EgervaryVertex>()
    private val setTComma = mutableSetOf<EgervaryVertex>()

    private fun adjustLabels() {
        saveStep(StepType.SkipPoint("Nem találtunk javítóutat, módosítanunk kell a címkéket"))
        setU.addAll(class1.filter { it.pair == null })
        saveStep(
            "Vegyük a következő halmazokat!\n" +
                "U: az A-beli párosítatlan csúcsok\n" +
                " \n" +
                " ",
        )
        setTComma.addAll(class2.filter { it in visitedVertices })
        saveStep(
            "Vegyük a következő halmazokat!\n" +
                "U: az A-beli párosítatlan csúcsok\n" +
                "T': a B-beli U-ból alternáló úton elérhető csúcsok\n" +
                " ",
        )
        setT.addAll(setTComma.mapNotNull { it.pair })
        saveStep(
            "Vegyük a következő halmazokat!\n" +
                "U: az A-beli párosítatlan csúcsok\n" +
                "T': a B-beli U-ból alternáló úton elérhető csúcsok\n" +
                "T: a T'-beli csúcsok párjai",
        )
        filteredClass1.addAll(setT)
        filteredClass1.addAll(setU)
        filteredClass2.addAll(class2 - setTComma)

        saveStep("Vegyük a T+U és a B-T' közti éleket")

        val delta =
            edges.filter {
                (it.toVertex in filteredClass1 && it.fromVertex in filteredClass2) ||
                    (it.fromVertex in filteredClass1 && it.toVertex in filteredClass2)
            }.minOfOrNull { it.fromVertex.label + it.toVertex.label - it.weight }
                ?: throw IllegalStateException("No edges found")

        deltaEdges.addAll(
            edges.filter {
                (it.toVertex in filteredClass1 && it.fromVertex in filteredClass2) ||
                    (it.fromVertex in filteredClass1 && it.toVertex in filteredClass2)
            }.filter { it.fromVertex.label + it.toVertex.label - it.weight == delta },
        )

        saveStep("δ = $delta")

        deltaEdges.clear()
        for (vertex in (setT + setU)) {
            vertex.label -= delta
        }
        for (vertex in setTComma) {
            vertex.label += delta
        }
        saveStep("Módosítottuk a címkéket")

        filteredClass1.clear()
        filteredClass2.clear()

        setU.clear()
        setT.clear()
        setTComma.clear()
    }

    var drawRed = false

    override fun toGraphicalGraph(stepType: StepType): GraphicalGraph {
        val drawVertexLabels = vertices.any { it.label != 0 }
        val graphicalVerticesMap = vertices.associateWith { vertex ->
            val coordinates = getVertexCoordinates(vertex)
            GraphicalVertex(
                x = coordinates.first,
                y = coordinates.second,
                name = vertex.name,
                label = if (drawVertexLabels) {
                    vertex.label.toString()
                } else {
                    null
                },
                highlight = when (vertex) {
                    activeVertex -> LIGHT_ORANGE
                    in unpairedVertices -> LIGHT_RED
                    in pairedVertices -> LIGHT_BLUE
                    in visitedVertices -> GRAY
                    else -> Color.Transparent
                },
                innerColor = when (vertex) {
                    in filteredClass1, in filteredClass2 -> {
                        LIGHT_GREEN
                    }

                    in setU -> RED

                    in setTComma -> BLUE

                    in setT -> PURPLE

                    else -> {
                        Color.White
                    }
                },
            )
        }
        val graphicalEdges = edges.map { edge ->
            GraphicalEdge(
                startGraphicalVertex = graphicalVerticesMap[edge.fromVertex]!!,
                endGraphicalVertex = graphicalVerticesMap[edge.toVertex]!!,
                selected = edge.selected,
                color = when {
                    edge.isRed && drawRed -> if (edge.weight == 0) LIGHT_RED else RED

                    (
                        edge.fromVertex in (filteredClass1 + filteredClass2) &&
                            edge.toVertex in (filteredClass1 + filteredClass2)
                        ) -> LIGHT_GREEN

                    edge.weight == 0 -> GRAY

                    else -> Color.Black
                },
                label = edge.weight.toString(),
                highlight = when (edge) {
                    in augmentingPathEdges -> LIGHT_YELLOW
                    in deltaEdges -> LIGHT_PINK
                    else -> Color.Transparent
                },
            )
        }
        return GraphicalGraph(
            graphicalVerticesMap.values.toList(),
            graphicalEdges,
            stepType,
        )
    }
}
