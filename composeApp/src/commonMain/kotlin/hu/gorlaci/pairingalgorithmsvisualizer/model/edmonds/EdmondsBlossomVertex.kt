package hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds

class EdmondsBlossomVertex(
    id: List<String>,
    type: EdmondsVertexType = EdmondsVertexType.NONE,
    pair: EdmondsVertex? = null,
    parent: EdmondsVertex? = null,
    val previousStructureVertices: List<EdmondsVertex>,
    val previousStructureEdges: Set<EdmondsEdge>,
) : EdmondsVertex(id, type, pair, parent) {

    override fun copy(): EdmondsVertex = EdmondsBlossomVertex(
        id = this.id,
        type = this.type,
        pair = this.pair,
        parent = this.parent,
        previousStructureVertices = this.previousStructureVertices,
        previousStructureEdges = this.previousStructureEdges,
    )
}
