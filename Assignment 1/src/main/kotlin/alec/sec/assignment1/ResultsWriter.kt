package alec.sec.assignment1

import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext

@ObsoleteCoroutinesApi
class ResultsWriter(private val resultsFile: File = File("results.csv")): CoroutineScope {

    override val coroutineContext = Dispatchers.IO

    fun writeResult(result: ComparisonResult) = launch {
        resultsFile.appendText("${result.file1},${result.file2},${result.similarity}\n")
    }

    fun clearFile() = launch { resultsFile.delete() }
}