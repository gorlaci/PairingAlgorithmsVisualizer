package hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds

import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex

open class EdmondsVertex(
    id: String,
    var type: VertexType = VertexType.NONE,
    var pair: EdmondsVertex? = null,
    var parent: EdmondsVertex? = null,
) : Vertex(id) {
    override fun toString(): String = "Vertex($id)"

    open fun copy(): EdmondsVertex =
        EdmondsVertex(
            id = this.id,
            type = this.type,
            pair = this.pair,
            parent = this.parent,
        )
}

enum class VertexType {
    ROOT,
    INNER,
    OUTER,
    CLEARING,
    NONE,
    ;

    fun isOuter() = this == OUTER || this == ROOT
}
