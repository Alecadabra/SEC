package alec.sec.assignment1

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File

@ObsoleteCoroutinesApi
@FlowPreview
class FileSearcher(
    private val rootDirectory: File,
    private val ui: UserInterface,
    private val resultsWriter: ResultsWriter = ResultsWriter(),
    private val similarityThreshold: Double = 0.5,
) {
    private fun File.isNotEmpty(): Boolean = this.reader().use { it.read() != -1 }

    private val fileFlow: Flow<File>
        get() = rootDirectory.absoluteFile.walkBottomUp().asFlow()
            .cancellable()
            // Only operate on files (They could be directories)
            .filter { file -> file.isFile }
            .filter { file ->
                setOf(".txt", ".md", ".java", ".kt", ".cs").any { ext -> file.name.endsWith(ext) }
            }
            .filter { file -> file.length() != 0L } // TODO
            .flowOn(Dispatchers.IO)

    // TODO Implement cancel functionality
    private val fileSearchFlow = this.fileFlow.flatMapConcat { file1 ->
        this.fileFlow
            // Don't compare a file to itself, and only compare each file once
            .filter { file2 -> file1.absolutePath > file2.absolutePath }
            .flowOn(Dispatchers.Default)
//            .map { file2 ->
//                ReadFile(file1, file1.readText()) to ReadFile(file2, file2.readText())
//            } // TODO
            .flowOn(Dispatchers.IO)
//            .map { (readFile1, readFile2) ->
//                ComparisonResult(
//                    readFile1.file.name,
//                    readFile2.file.name,
//                    calcSimilarity(readFile1.string, readFile2.string),
//                )
//            }TODO
            .map { file2 ->
                ComparisonResult(
//TODO Remove
                    file1.name,
                    file2.name,
                    calcSimilarity(file1.readText(), file2.readText()),
                )
            }
            .flowOn(Dispatchers.Default)
//            .onEach { result -> this.resultsWriter.writeResult(result) }TODO
            .flowOn(Dispatchers.IO)
            .onEach { this.ui.progress++ }
            .flowOn(Dispatchers.Main)
    }
        .filter { result -> result.similarity >= similarityThreshold }
//        .onEach { result -> this.ui.addComparisonResult(result) }
        .catch { e -> println("An error happened: ${e.message}") }
        .flowOn(Dispatchers.Main)

    suspend fun launchIn(scope: CoroutineScope): Job {
        // The number of comparisons is half size of the n * n comparison matrix, with the leading
        // diagonal removed, where n is the number of files after filtering.
        val numComparisons = withContext(Dispatchers.IO) {
            this@FileSearcher.fileFlow.count().let { (it * it - it) / 2 }
        }

        // Set the total value for the UI progress bar
        withContext(Dispatchers.Main) { this@FileSearcher.ui.progressTotal = numComparisons }

        // Run and terminate the flow
//        return scope.launch {
//            this@FileSearcher.fileSearchFlow.collect { result ->
//                this@FileSearcher.ui.addComparisonResult(result)
//            }
//        }
        this@FileSearcher.fileSearchFlow.collect { result ->
            withContext(Dispatchers.Main) {
                this@FileSearcher.ui.addComparisonResult(result)
            }
        }
        return Job()
    }
}

data class ReadFile(val file: File, val string: String)