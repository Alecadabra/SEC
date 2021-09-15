package alec.sec.assignment1

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.io.File

/**
 * Handles writing [ComparisonResult]s to a [resultsFile] via the [addResult] function.
 * Operates in its own contained IO thread.
 */
@ObsoleteCoroutinesApi
class ResultsFileWriter(private val resultsFile: File = File("results.csv")) : CoroutineScope {

    init {
        // Clear the file on initialisation
        resultsFile.delete()
    }

    // Provides the single thread to do all writes on (No need for mutex when there's only one
    // thread)
    override val coroutineContext = newSingleThreadContext(this.resultsFile.name)

    /**
     * Appends a new [result] to the file, in comma separated format.
     */
    fun addResult(result: ComparisonResult) = launch {
        val (file1, file2, similarity) = result
        resultsFile.appendText("$file1,$file2,$similarity\n")
    }
}