package alec.sec.assignment1

import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import kotlinx.coroutines.*
import java.io.File
import kotlin.properties.Delegates

@ObsoleteCoroutinesApi
@FlowPreview
class UserInterface : Application() {
    private val resultTable = TableView<ComparisonResult>()
    private val progressBar = ProgressBar()

    var progress: Int by Delegates.observable(0) { _, _, newVal ->
        this.progressBar.progress = newVal.toDouble() / progressTotal
    }
    var progressTotal = Int.MAX_VALUE

    var searchJob: Job? = null

    override fun start(stage: Stage) {
        stage.title = "Alec Assignment 1"
        stage.minWidth = 600.0

        // Create toolbar
        val compareBtn = Button("Compare...")
        val stopBtn = Button("Stop")
        val toolBar = ToolBar(compareBtn, stopBtn)

        // Set up button event handlers.
        compareBtn.onAction = EventHandler { crossCompare(stage) }
        stopBtn.onAction = EventHandler { stopComparison() }

        // Initialise progressbar
        progressBar.progress = 0.0
        val file1Col = TableColumn<ComparisonResult, String>("File 1")
        val file2Col = TableColumn<ComparisonResult, String>("File 2")
        val similarityCol = TableColumn<ComparisonResult, String>("Similarity")

        // The following tells JavaFX how to extract information from a ComparisonResult
        // object and put it into the three table columns.
        file1Col.setCellValueFactory { cell ->
            SimpleStringProperty(cell.value.file1)
        }
        file2Col.setCellValueFactory { cell ->
            SimpleStringProperty(cell.value.file2)
        }
        similarityCol.setCellValueFactory { cell ->
            SimpleStringProperty(
                String.format("%.1f%%", cell.value.similarity * 100.0)
            )
        }

        // Set and adjust table column widths.
        file1Col.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.40))
        file2Col.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.40))
        similarityCol.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.20))

        // Add the columns to the table.
        resultTable.columns.add(file1Col)
        resultTable.columns.add(file2Col)
        resultTable.columns.add(similarityCol)

        // Add the main parts of the UI to the window.
        val mainBox = BorderPane().also {
            it.top = toolBar
            it.center = resultTable
            it.bottom = progressBar
        }
        val scene = Scene(mainBox)
        stage.scene = scene
        stage.sizeToScene()
        stage.show()
    }

    fun addComparisonResult(comparisonResult: ComparisonResult) {
        this.resultTable.items.add(comparisonResult)
    }

    private fun crossCompare(stage: Stage) {
        this.resultTable.items.clear()
        this.progress = 0
        this.progressTotal = Int.MAX_VALUE
        this.searchJob = null

        val dc = DirectoryChooser().also {
            it.initialDirectory = File(".")
            it.title = "Choose directory"
        }
        val directory: File? = dc.showDialog(stage)
        if (directory != null) {
            println("Comparing files within $directory...")

            this.searchJob = runBlocking(Dispatchers.IO) {
                val resultsWriter = ResultsWriter().also { it.clearFile() }

                val fileSequence = FileSearcher(directory, ui = this@UserInterface, resultsWriter = resultsWriter)

                fileSequence.launchIn(this)
            }
        }
    }

    private fun stopComparison() {
        println("Stopping comparison...")
        this.searchJob?.cancel()
    }

    companion object {
        /** The main method to launch the JavaFX app */
        fun main(args: Array<String>) = launch(*args)
    }
}