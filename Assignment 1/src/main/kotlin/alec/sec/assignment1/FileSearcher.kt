package alec.sec.assignment1

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File

/**
 * Handles asynchronously searching recursively through the given [rootDirectory] and comparing
 * every file to each other, updating results on the [ui] and [resultsWriter], with a given
 * [similarityThreshold] for displaying results in the [ui] table
 */
@ObsoleteCoroutinesApi
@FlowPreview
class FileSearcher(
    private val rootDirectory: File,
    private val ui: UserInterface,
    private val resultsWriter: ResultsFileWriter = ResultsFileWriter(),
    private val similarityThreshold: Double = 0.5,
) {

    private var startTime: Long = 0L

    // Run and terminate the entire search flow
    suspend fun run() = this.fileSearchFlow.collect()

    // A flow of all valid files to search through
    private val fileFlow: Flow<File>
        // Recursive walk through the contents of the directory
        get() = rootDirectory.absoluteFile.walkBottomUp().asFlow()
            // IO: Only operate on files (They could be directories)
            .filter { file -> file.isFile }
            // IO: Only support 'text' file extensions
            .filter { file ->
                setOf(".txt", ".md", ".java", ".kt", ".cs").any { ext -> file.name.endsWith(ext) }
            }
            // IO: Skip empty files
            .filter { file -> file.reader().use { it.read() != -1 } }
            // Run above operations in the IO context
            .flowOn(Dispatchers.IO)

    // A flow that handles all parts of the file search, fundamentally by combining each element
    // of fileFlow with another fileFlow.
    private val fileSearchFlow = this.fileFlow.flatMapConcat { file1 ->
        this.fileFlow
            // CPU: Don't compare a file to itself, and only compare each file once
            .filter { file2 -> file1.absolutePath > file2.absolutePath }
            // Run above operations in the CPU context
            .flowOn(Dispatchers.Default)
            // IO: Read in the files into strings, and then into short-lived data classes
            .map { file2 ->
                ReadFile(file1, file1.readText()) to ReadFile(file2, file2.readText())
            }
            // Run above operations in the IO context
            .flowOn(Dispatchers.IO)
            // Run the above IO code and below UI code in parallel
            .buffer()
            // CPU: Compare the files and package into a comparison result
            .map { (readFile1, readFile2) ->
                ComparisonResult(
                    readFile1.file.name,
                    readFile2.file.name,
                    calcSimilarity(readFile1.string, readFile2.string),
                )
            }
            // CPU: Send the result to the resultsWriter (Uses its own UI thread)
            .onEach { result -> this.resultsWriter.addResult(result) }
            // Run above operations in the CPU context
            .flowOn(Dispatchers.Default)
            // Run the above CPU code and below UI code in parallel
            .buffer()
            // UI: Update the progress bar value
            .onEach { this.ui.progress++ }
            // Check for a cancelled job and throw CancellationException (Like InterruptedException)
            .cancellable()
            // Run above operations in the GUI context
            .flowOn(Dispatchers.Main)
    }
        // CPU: Only display results above the threshold
        .filter { result -> result.similarity >= similarityThreshold }
        // Run above operations in the CPU context
        .flowOn(Dispatchers.Main)
        // UI: Add the comparison result to the gui's table
        .onEach { result -> this.ui.results.add(result) }
        // Run above operations in the GUI context
        .flowOn(Dispatchers.Main)
        .onStart {
            ui.status = "Counting files"
            startTime = System.currentTimeMillis()

            // The number of comparisons is half size of the n * n comparison matrix, with the
            // leading diagonal removed, where n is the number of files after filtering.
            val numFiles = fileFlow.count()
            val numComparisons = numFiles.let { (it * it - it) / 2 }

            // Set the total value for the UI progress bar
            withContext(Dispatchers.Main) {
                ui.progressTotal = numComparisons
                ui.progress = 0
                ui.status = "Searching through $numFiles files ($numComparisons comparisons)"
            }
        }
        // Handle the cases of completion (Equivalent of handling InterruptedException)
        .onCompletion { e: Throwable? ->
            val time = System.currentTimeMillis() - startTime
            ui.status = when (e) {
                null -> when {
                    time > 60_000 -> "Finally done after ${time / 1000 / 60} minutes"
                    time > 1_000 -> "Done in ${time / 1000}s"
                    else -> "Done in only ${time}ms!"
                }
                is CancellationException -> "Stopped!"
                else -> "Error! ${e.message}"
            }
            resultsWriter.closeContext()
        }

    // Represents a file and it's contents as a string
    private data class ReadFile(val file: File, val string: String)
}