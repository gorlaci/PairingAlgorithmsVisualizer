package hu.gorlaci.pairingalgorithmsvisualizer.util

fun withArticle(word: String): String {
    val firstChar = word[0].lowercaseChar()
    return when (firstChar) {
        'a', 'á', 'e', 'é', 'i', 'í', 'o', 'ó', 'ö', 'ő', 'u', 'ú', 'ü', 'ű' -> "az $word"
        else -> "a $word"
    }
}
