package alec.sec.assignment1

/**
 * Simply holds the data for a similarity score between two files. [file1] and [file2] are
 * filenames, and [similarity] is between 0 and 1.
 */
data class ComparisonResult(val file1: String, val file2: String, val similarity: Double)