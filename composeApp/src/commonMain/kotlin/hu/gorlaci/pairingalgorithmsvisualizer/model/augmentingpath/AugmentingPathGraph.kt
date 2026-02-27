package hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath

import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph

class AugmentingPathGraph(
    override val vertices: MutableSet<AugmentingPathVertex> = mutableSetOf(),
    name: String = "",
): Graph( name, vertices, mutableSetOf() ) {


    fun runAlgorithm(){
        var nextVertex = getNextVertex()
        while (nextVertex != null) {
            findAugmentingPath(nextVertex)
            nextVertex = getNextVertex()
        }
    }

    fun findAugmentingPath( startVertex: AugmentingPathVertex ){
        val unpairedVertices = mutableSetOf( startVertex )
        val pairedVertices = mutableSetOf<AugmentingPathVertex>()
        while( unpairedVertices.isNotEmpty() ) {
            for (vertex in unpairedVertices) {
                vertex.visited = true
                for (neighbour in vertex.neighbours.filter { !it.visited }) {
                    if (neighbour.pair == null) {
                        augmentFromVertex(neighbour)
                        reset()
                        return
                    } else {
                        neighbour.parent = vertex
                        pairedVertices.add(neighbour)
                    }
                }
            }
            unpairedVertices.clear()
            for( vertex in pairedVertices ){
                vertex.visited = true
                if( vertex.pair?.visited == false) {
                    vertex.pair?.parent = vertex
                    unpairedVertices.add(vertex.pair!!)
                }
            }
        }
    }

    private fun getNextVertex(): AugmentingPathVertex? {
        return vertices.firstOrNull { !it.visited && it.pair == null }
    }

    private fun reset(){
        for( vertex in vertices ){
            vertex.visited = false
            vertex.parent = null
        }
    }

    private fun augmentFromVertex( vertex: AugmentingPathVertex ){
        var current: AugmentingPathVertex? = vertex
        while( current != null ){
            val parent = current.parent
            if( parent != null ) {
                val grandParent = parent.parent
                parent.pair = current
                current.pair = parent
                current = grandParent
            } else {
                break
            }
        }
    }
}

fun Graph.asAugmentingPathGraph(): AugmentingPathGraph {
    val augmentingPathVertices = vertices.map { vertex ->
        AugmentingPathVertex(
            id = vertex.id,
            neighbours = edges.filter { it.fromVertex == vertex }.map { it.toVertex as AugmentingPathVertex }.toSet() +
                    edges.filter { it.toVertex == vertex }.map { it.fromVertex as AugmentingPathVertex }.toSet(),
        )
    }.toMutableSet()
    return AugmentingPathGraph(
        vertices = augmentingPathVertices,
        name = name,
    )
}