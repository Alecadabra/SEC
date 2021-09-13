package alec.sec.assignment1

/**
 * Gets the similarity of two strings as a value between 0.0 (Not similar) and 1.0 (Identical).
 */
fun calcSimilarity(s1: String, s2: String): Double {
//    // Gets the length of the longest common subsequence of two strings.
//    fun lcs(s1: String, s2: String): String = when {
//        // Base case - If both strings are empty, the lcs is 0
//        s1.isEmpty() || s2.isEmpty() -> ""
//
//        // If the ends are equal, add 1 and recurse without the last character
//        s1.last() == s2.last() -> lcs(
//            s1.dropLast(1),
//            s2.dropLast(1)
//        ) + s1.last()
//
//        // Else, take the max value from recursion without the last character from each string
//        else -> {
//            val lcs1 = lcs(s1, s2.dropLast(1))
//            val lcs2 = lcs(s1.dropLast(1), s2)
//            if (lcs1.length > lcs2.length) lcs1 else lcs2
//        }
//    }

//    fun lcs(x: String, y: String): String {
//        if (x.length == 0 || y.length == 0) return ""
//        val x1 = x.dropLast(1)
//        val y1 = y.dropLast(1)
//        if (x.last() == y.last()) return lcs(x1, y1) + x.last()
//        val x2 = lcs(x, y1)
//        val y2 = lcs(x1, y)
//        return if (x2.length > y2.length) x2 else y2
//    }

    fun lcs(x: String, y: String): String = when {
        x.isNotEmpty() -> lcs(x.dropLast(1), y)
        else -> x
    }

    // The similarity score is the proportion of the strings that is made up by the common
    // subsequence
    return (lcs(s1, s2).length * 2.0) / (s1.length + s2.length)
}