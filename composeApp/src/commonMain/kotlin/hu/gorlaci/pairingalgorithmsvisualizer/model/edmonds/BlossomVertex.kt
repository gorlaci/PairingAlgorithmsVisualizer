package hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds

class BlossomVertex(
    id: List<String>,
    type: EdmondsVertexType = EdmondsVertexType.NONE,
    pair: EdmondsVertex? = null,
    parent: EdmondsVertex? = null,
    val previousStructureVertices: List<EdmondsVertex>,
    val previousStructureEdges: Set<EdmondsEdge>,
) : EdmondsVertex(id, type, pair, parent) {

    constructor(
        id: String,
        type: EdmondsVertexType = EdmondsVertexType.NONE,
        pair: EdmondsVertex? = null,
        parent: EdmondsVertex? = null,
        previousStructureVertices: List<EdmondsVertex>,
        previousStructureEdges: Set<EdmondsEdge>,
    ) : this(
        id = listOf(id),
        type = type,
        pair = pair,
        parent = parent,
        previousStructureVertices = previousStructureVertices,
        previousStructureEdges = previousStructureEdges,
    )

    override fun copy(): EdmondsVertex = BlossomVertex(
        id = this.id,
        type = this.type,
        pair = this.pair,
        parent = this.parent,
        previousStructureVertices = this.previousStructureVertices,
        previousStructureEdges = this.previousStructureEdges,
    )
}
