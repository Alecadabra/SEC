package alec.sec.assignment1

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.File

@FlowPreview
class FileSearcher(
    private val rootDirectory: File,
    private val ui: UserInterface,
    private val similarityThreshold: Double = 0.5,
) {
    private val fileFlow: Flow<File>
        get() = rootDirectory.absoluteFile.walkBottomUp().asFlow()
            .cancellable()
            // Only operate on files (They could be directories)
            .filter { file -> file.isFile }
            .filter { file ->
                setOf(".txt", ".md", ".java", ".kt", ".cs").any { ext -> file.name.endsWith(ext) }
            }
            .filter { file -> file.length() != 0L } // TODO Implement cheaper empty check
            .flowOn(Dispatchers.IO)

    // TODO Implement cancel functionality
    private val fileSearchFlow = this.fileFlow.flatMapConcat { file1 ->
        this.fileFlow
            // The upper right sub matrix of the logical comparison matrix with the leading
            // diagonal removed. Ensures files are only compared once and are not compared
            // to themselves
            .filter { file2 -> file1.absolutePath > file2.absolutePath }
            .map { file2 ->
                ComparisonResult(
                    file1.name,
                    file2.name,
                    calcSimilarity(file1.readText(), file2.readText()),
                )
            }
            //.onEach { delay(1000) } // TODO Remove
            //.onEach {  } // TODO Write to results.csv
            .flowOn(Dispatchers.IO)
            .onEach { this.ui.progress++ }
            .flowOn(Dispatchers.Main)
    }
        .filter { result -> result.similarity >= similarityThreshold }
        .catch { e -> println("An error happened: ${e.message}") }

    suspend fun start() {
        // The number of comparisons is half size of the n * n comparison matrix, with the leading
        // diagonal removed, where n is the number of files after filtering.
        val numComparisons = this.fileFlow.count().let { (it * it - it) / 2 }

        // Set the total value for the UI progress bar
        withContext(Dispatchers.Main) {
            this@FileSearcher.ui.progressTotal = numComparisons
        }

        // Collect up results from fileSearchFlow and send to UI
        this.fileSearchFlow.collect { result ->
            withContext(Dispatchers.Main) {
                this@FileSearcher.ui.addComparisonResult(result)
            }
        }
    }
}
