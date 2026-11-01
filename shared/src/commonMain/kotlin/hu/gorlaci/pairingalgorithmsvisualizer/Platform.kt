package hu.gorlaci.pairingalgorithmsvisualizer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
