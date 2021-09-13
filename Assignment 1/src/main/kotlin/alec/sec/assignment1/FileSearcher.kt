package alec.sec.assignment1

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.File

@FlowPreview
class FileSearcher(
    private val rootDirectory: File,
    private val userInterface: UserInterface,
) {
    private val fileFlow = rootDirectory.walkBottomUp().asFlow().cancellable()
        // IO Stuff to come
        .flowOn(Dispatchers.IO)
        // Only operate on files (They could be directories)
        .filter { file -> file.isFile }
        .filter { file ->
            setOf(".txt", ".md", ".java", ".kt", ".cs").any { ext -> file.name.endsWith(ext) }
        }
        .filter { file -> file.length() != 0L }
        .flatMapConcat { file1 ->
            rootDirectory.walkBottomUp().asFlow().cancellable()
                .flowOn(Dispatchers.IO)
                // Only operate on files (They could be directories)
                .filter { file2 -> file2.isFile }
                .filter { file2 ->
                    setOf(".txt", ".md", ".java", ".kt", ".cs").any { ext ->
                        file2.name.endsWith(ext)
                    }
                }
                .filter { file2 -> file2.length() != 0L }
                .filter { file2 -> file1.absolutePath < file2.absolutePath }
                .map { file2 ->
                    val string1 = file1.readText()
                    val string2 = file2.readText()
                    val similarity = calcSimilarity(string1, string2)

                    ComparisonResult(file1.name, file2.name, similarity)
                }
        }
        .catch { e -> println("An error happened: ${e.message}") }

    suspend fun start() {
        this.fileFlow.collect { result ->
            withContext(Dispatchers.Main) {
                this@FileSearcher.userInterface.addComparisonResult(result)
            }
        }
    }
}
