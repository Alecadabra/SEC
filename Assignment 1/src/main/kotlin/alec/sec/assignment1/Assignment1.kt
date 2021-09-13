package alec.sec.assignment1

import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import java.io.File

class Assignment1 : Application() {
    private val resultTable = TableView<ComparisonResult>()
    private val progressBar = ProgressBar()

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

    private fun crossCompare(stage: Stage) {
        val dc = DirectoryChooser().also {
            it.initialDirectory = File(".")
            it.title = "Choose directory"
        }
        val directory = dc.showDialog(stage)
        println("Comparing files within $directory...")

        // Extremely fake way of demonstrating how to use the progress bar (noting that it can 
        // actually only be set to one value, from 0-1, at a time.)
        progressBar.progress = 0.25
        progressBar.progress = 0.5
        progressBar.progress = 0.6
        progressBar.progress = 0.85
        progressBar.progress = 1.0

        // Extremely fake way of demonstrating how to update the table (noting that this shouldn't
        // just happen once at the end, but progressively as each result is obtained.)
        val newResults = listOf(
            ComparisonResult("Example File 1", "Example File 3", 0.31),
            ComparisonResult("Example File 2", "Example File 3", 0.45),
            ComparisonResult("Example File 1", "Example File 2", 0.75)
        )
        resultTable.items.setAll(newResults)

        // progressBar.setProgress(0.0); // Reset progress bar after successful comparison?
    }

    private fun stopComparison() {
        println("Stopping comparison...")
    }

    companion object {
        /** The main method to launch the JavaFX app */
        fun main(args: Array<String>) = launch(*args)
    }
}