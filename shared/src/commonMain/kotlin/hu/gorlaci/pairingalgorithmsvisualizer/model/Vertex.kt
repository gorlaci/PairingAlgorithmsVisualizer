package hu.gorlaci.pairingalgorithmsvisualizer.model

open class Vertex(
    val id: List<String>,
) {
    constructor(id: String) : this(listOf(id))

    val name: String
        get() = id.joinToString("")
}
