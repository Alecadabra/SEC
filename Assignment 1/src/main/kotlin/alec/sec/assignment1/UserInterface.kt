package alec.sec.assignment1

import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import kotlinx.coroutines.*
import java.io.File

/**
 * Handles the GUI for the program with public fields to edit to update the UI.
 */
@ObsoleteCoroutinesApi
@FlowPreview
class UserInterface : Application() {
    // UI Components
    private val resultTable = TableView<ComparisonResult>()
    private val progressBar = ProgressBar()
    private val statusView = Text("Waiting")

    // Publicly configurable UI backing values
    var progressTotal = Int.MAX_VALUE
    var progress: Int = 0
        set(value) {
            this.progressBar.progress = value.toDouble() / progressTotal
            field = value
        }
    val results: MutableList<ComparisonResult>
        get() = this.resultTable.items
    var status: String
        get() = this.statusView.text
        set(value) {
            println("Status: $value")
            this.statusView.text = value
        }

    // The job the file searcher runs on, so it can be cancelled
    private var searchJob: Job? = null

    override fun start(stage: Stage) {
        stage.title = "Alec Assignment 1"
        stage.minWidth = 600.0

        // Create toolbar
        val compareBtn = Button("Compare")
        val stopBtn = Button("Stop")
        val toolBar = ToolBar(compareBtn, stopBtn)
        val footerBar = ToolBar(progressBar, Separator(), statusView)

        // Set up button event handlers.
        compareBtn.onAction = EventHandler { onCompare(stage) }
        stopBtn.onAction = EventHandler { onStop() }

        // Initialise progressbar
        progressBar.progress = 0.0

        // Set up table columns
        TableColumn<ComparisonResult, String>("File 1").also { file1 ->
            file1.setCellValueFactory { cell -> SimpleStringProperty(cell.value.file1) }
            file1.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.40))
            resultTable.columns.add(file1)
        }

        TableColumn<ComparisonResult, String>("File 2").also { file2 ->
            file2.setCellValueFactory { cell -> SimpleStringProperty(cell.value.file2) }
            file2.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.40))
            resultTable.columns.add(file2)
        }
        TableColumn<ComparisonResult, String>("Similarity").also { similarity ->
            similarity.setCellValueFactory { cell ->
                SimpleStringProperty(
                    String.format("%.1f%%", cell.value.similarity * 100.0)
                )
            }
            similarity.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.20))
            resultTable.columns.add(similarity)
        }

        // Add the main parts of the UI to the window.
        val mainBox = BorderPane().also {
            it.top = toolBar
            it.center = resultTable
            it.bottom = footerBar
        }
        val scene = Scene(mainBox)
        stage.scene = scene
        stage.sizeToScene()
        stage.show()
    }

    // Callback for the "Compare" button
    private fun onCompare(stage: Stage) {
        this.resultTable.items.clear()
        this.progress = 0
        this.progressTotal = Int.MAX_VALUE
        this.searchJob = null

        val directory: File? = DirectoryChooser().also {
            it.initialDirectory = File(".")
            it.title = "Choose directory"
        }.showDialog(stage)

        if (directory != null) {
            println("Comparing files within $directory...")

            val resultsWriter = ResultsFileWriter()

            val fileSequence = FileSearcher(
                rootDirectory = directory,
                ui = this@UserInterface,
                resultsWriter = resultsWriter
            )

            this.searchJob = CoroutineScope(Dispatchers.IO).launch {
                fileSequence.run()
            }
        }
    }

    // Callback for the "Stop" button
    private fun onStop() {
        if (this.searchJob?.isActive == true) {
            this.status = "Stopping..."
            this.searchJob?.cancel()
        }
    }

    companion object {
        /** The main method to launch the JavaFX app */
        fun main(args: Array<String>) = launch(*args)
    }
}