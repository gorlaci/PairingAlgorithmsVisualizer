package hu.gorlaci.pairingalgorithmsvisualizer.model

open class Graph(
    var name: String = "",
    open val vertices: MutableSet<out Vertex> = mutableSetOf(),
    open val edges: MutableSet<out Edge> = mutableSetOf(),
){
    val isBipartite: Boolean
        get() {
            if( vertices.size < 3 ) {
                return true
            }
            val unvisited = vertices.toMutableSet()
            val queue = ArrayDeque<Vertex>()
            val colorMap = mutableMapOf<Vertex, Boolean>()
            while (unvisited.isNotEmpty()) {
                val start = unvisited.first()
                queue.add(start)
                colorMap[start] = true
                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst()
                    unvisited.remove(current)
                    val currentColor = colorMap[current] ?: continue
                    val neighbours = edges.filter { it.fromVertex == current && it.toVertex in unvisited }.map { it.toVertex } +
                            edges.filter { it.toVertex == current && it.fromVertex in unvisited }.map { it.fromVertex }
                    for (neighbour in neighbours) {
                        if (neighbour in colorMap) {
                            if (colorMap[neighbour] == currentColor) {
                                return false
                            }
                        } else {
                            colorMap[neighbour] = !currentColor
                            queue.add(neighbour)
                        }
                    }
                }
            }
            return true
        }
}