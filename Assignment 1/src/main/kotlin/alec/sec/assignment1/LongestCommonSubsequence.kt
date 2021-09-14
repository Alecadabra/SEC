package alec.sec.assignment1

/**
 * Gets the similarity of two strings as a value between 0.0 (Not similar) and 1.0 (Identical).
 */
fun calcSimilarity(s1: String, s2: String): Double {
    // Longest common subsequence dynamic programming algorithm
    val subSolutions = Array(s1.length + 1) {
        IntArray(s2.length + 1) { 0 }
    }
    val directionLeft = Array(s1.length + 1) {
        BooleanArray(s2.length + 1) { false }
    }

    (1..s1.length).forEach { i ->
        (1..s2.length).forEach { j ->
            when {
                s1[i - 1] == s2[j - 1] -> {
                    subSolutions[i][j] = subSolutions[i - 1][j - 1] + 1
                }
                subSolutions[i - 1][j] > subSolutions[i][j - 1] -> {
                    subSolutions[i][j] = subSolutions[i - 1][j]
                    directionLeft[i][j] = true
                }
                else -> {
                    subSolutions[i][j] = subSolutions[i][j - 1]
                    directionLeft[i][j] = false
                }
            }
        }
    }

    var matches = 0
    var i = s1.length
    var j = s2.length

    while (i > 0 && j > 0) when {
        s1[i - 1] == s2[j - 1] -> {
            matches++
            i--
            j--
        }
        directionLeft[i][j] -> i--
        else -> j--
    }

    // The similarity score is the proportion of the strings that is made up by the common
    // subsequence
    return (matches * 2.0) / (s1.length + s2.length)
}