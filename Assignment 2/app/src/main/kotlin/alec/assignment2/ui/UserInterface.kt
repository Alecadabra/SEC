package alec.assignment2.ui

import alec.assignment2.i18n.Translation
import alec.assignment2.io.FileIO
import alec.assignment2.io.FileIO.Encoding
import alec.assignment2.keymap.KeymapController
import alec.assignment2.keymap.KeymapException
import alec.assignment2.keymap.KeymapParser
import alec.assignment2.keymap.KeymapTextAction
import alec.assignment2.plugin.*
import javafx.application.Application
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.stage.Window
import texteditor.api.EditorPlugin
import java.io.File
import java.io.IOException
import java.util.*

class UserInterface : Application() {

    // I18N
    /** Contains the localised strings to display on the user interface. */
    private val translation = Translation()
    // ButtonTypes with l10n
    private val okButtonType: ButtonType get() = ButtonType(
        translation.ok,
        ButtonBar.ButtonData.OK_DONE
    )
    private val cancelButtonType: ButtonType get() = ButtonType(
        translation.cancel,
        ButtonBar.ButtonData.CANCEL_CLOSE
    )

    // UI Elements
    private val textArea = TextArea()
    private val toolBar = ToolBar()

    // Plugin & script management
    private val pluginController = PluginController(this)
    private val keyStrokeListeners = mutableMapOf<KeyStroke, () -> Unit>()

    // File IO
    private val fileIO by lazy { FileIO(textController) }

    // Access to text area
    val textController: TextInputControl
        get() = this.textArea

    override fun init() {
        // Set default locale from parameters
        parameters.named["locale"]?.let { tag -> Locale.forLanguageTag(tag) }?.also { locale ->
            Locale.setDefault(locale)
        }

        super.init()
    }

    override fun start(stage: Stage) {
        // Subtle user experience tweaks
        toolBar.isFocusTraversable = false
        toolBar.items.forEach { btn: Node -> btn.isFocusTraversable = false }
        textArea.style = "-fx-font-family: 'monospace'" // Set the font

        // Parse keymap
        loadKeymap()

        // Load plugins
        loadPluginsDialog(stage).forEach { plugin ->
            this.pluginController.startPlugin(plugin)
        }

        // Place FileIO buttons
        this.toolBar.items.addAll(
            Button(translation.fileSave).also { it.setOnAction { saveFile(stage) } },
            Button(translation.fileLoad).also { it.setOnAction { loadFile(stage) } }
        )

        stage.also { stg ->
            stg.title = "Text Editor"
            stg.minWidth = 800.0

            stg.scene = Scene(
                BorderPane().also { pane ->
                    pane.top = toolBar
                    pane.center = textArea
                }
            ).also { scene ->
                scene.setOnKeyPressed { keyEvent ->
                    this.keyStrokeListeners[keyEvent.asKeyStroke]?.invoke()
                }
            }

            stg.sizeToScene()
            stg.show()
        }
    }

    private fun loadKeymap() {
        try {
            val text = File("../keymap").readText()
            val map = KeymapController(text).parse()

            map.onEach { (keyStroke, action) ->
                keyStrokeListeners[keyStroke] = {
                    // Sadly ran out of time for the logic, so here's an alert
                    Alert(
                        Alert.AlertType.INFORMATION,
                        """
                            You pressed $keyStroke!
                            The logic for this isn't implemented sadly
                            Normally this would ${action.action} '${action.text}'
                            from the ${action.position}
                        """.trimIndent(),
                        this.okButtonType
                    ).showAndWait()
                }
            }
        } catch (e: IOException) {
            Alert(
                Alert.AlertType.ERROR,
                e.message,
                this.okButtonType
            ).showAndWait()
        } catch (e: KeymapException) {
            Alert(
                Alert.AlertType.ERROR,
                e.message,
                this.okButtonType
            ).showAndWait()
        }
    }

    /**
     * Creates, shows, and returns the answer to a dialog allowing the loading of plugins
     * and scripts.
     */
    private fun loadPluginsDialog(window: Window) = Dialog<List<EditorPlugin>>().also { dialog ->
        val pluginList = FXCollections.observableArrayList<EditorPlugin>()

        dialog.title = translation.appName
        dialog.headerText = translation.loadPluginsAndScripts
        dialog.dialogPane.content = BorderPane().also { box ->
            box.top = ToolBar(
                // Load Script Button
                Button(translation.loadScript).also {
                    it.setOnAction {
                        // Select a python script file
                        FileChooser().also { fileChooser ->
                            fileChooser.initialDirectory = File(".")
                            fileChooser.title = translation.scriptLocationInput
                            fileChooser.extensionFilters.addAll(
                                FileChooser.ExtensionFilter(
                                    translation.filesPython,
                                    "*.py"
                                ),
                                FileChooser.ExtensionFilter(
                                    translation.filesAll,
                                    "*.*"
                                )
                            )
                            fileChooser.showOpenDialog(window)?.also { file ->
                                try {
                                    pluginList.add(PluginLoader.loadScript(file.absolutePath))
                                } catch (e: PluginLoaderException) {
                                    Alert(
                                        Alert.AlertType.ERROR,
                                        e.message,
                                        this.okButtonType
                                    ).showAndWait()
                                }
                            }
                        }
                    }
                },
                // Load plugin button
                Button(translation.loadPlugin).also {
                    it.setOnAction {
                        // Get the fully qualified class name of the EditorPlugin implementation
                        TextInputDialog().also { textDialog ->
                            textDialog.title = translation.loadPlugin
                            textDialog.headerText = translation.pluginNameInput
                            textDialog.resultProperty().addListener { _, _, clsName ->
                                try {
                                    pluginList.add(PluginLoader.loadPlugin(clsName))
                                } catch (e: PluginLoaderException) {
                                    Alert(
                                        Alert.AlertType.ERROR,
                                        e.message,
                                        this.okButtonType
                                    ).showAndWait()
                                }
                            }
                        }.showAndWait()
                    }
                }
            )
            // List of all loaded plugins
            box.center = ListView(pluginList).also { listView ->
                // Display EditorPlugin.name rather than EditorPlugin.toString
                listView.setCellFactory {
                    object : ListCell<EditorPlugin>() {
                        override fun updateItem(item: EditorPlugin?, empty: Boolean) {
                            super.updateItem(item, empty)
                            text = (if (empty) null else item?.name)
                        }
                    }
                }
            }
        }
        dialog.dialogPane.buttonTypes.add(this.okButtonType)
        dialog.setResultConverter { buttonType ->
            when (buttonType.buttonData) {
                ButtonBar.ButtonData.OK_DONE -> pluginList
                else -> null
            }
        }
    }.showAndWait().orElse(listOf())!!

    private fun selectEncodingDialog(): Encoding? = Dialog<Encoding>().also { dialog ->
        val comboBox = ComboBox<Encoding>().also { comboBox ->
            comboBox.items.setAll(*Encoding.values())
            comboBox.value = Encoding.values().first()
        }

        dialog.title = translation.fileIO
        dialog.headerText = translation.fileSelectEncoding
        dialog.dialogPane.content = comboBox
        dialog.dialogPane.buttonTypes.addAll(okButtonType, cancelButtonType)
        dialog.setResultConverter { btnType ->
            when(btnType.buttonData) {
                ButtonBar.ButtonData.OK_DONE -> comboBox.value
                else -> null
            }
        }
    }.showAndWait().orElse(null)

    private fun saveFile(window: Window) {
        selectEncodingDialog()?.let { encoding ->
            FileChooser().let { fileChooser ->
                fileChooser.initialDirectory = File(".")
                fileChooser.title = translation.fileLoad
                fileChooser.extensionFilters.add(
                    FileChooser.ExtensionFilter(translation.filesAll, "*.*"),
                )
                fileChooser.showSaveDialog(window)
            }?.let { file ->
                try {
                    this.fileIO.save(file.absolutePath, encoding)
                } catch (e: IOException) {
                    Alert(
                        Alert.AlertType.ERROR, e.message, this.okButtonType
                    ).showAndWait()
                }
            }
        }
    }

    private fun loadFile(window: Window) {
        selectEncodingDialog()?.also { encoding ->
            FileChooser().let { fileChooser ->
                fileChooser.initialDirectory = File(".")
                fileChooser.title = translation.fileLoad
                fileChooser.extensionFilters.add(
                    FileChooser.ExtensionFilter(translation.filesAll, "*.*")
                )
                fileChooser.showOpenDialog(window)
            }?.also { file ->
                try {
                    this.textController.text = this.fileIO.load(file.absolutePath, encoding)
                } catch (e: IOException) {
                    Alert(
                        Alert.AlertType.ERROR, e.message, this.okButtonType
                    ).showAndWait()
                }
            }
        }
    }

    fun addButton(text: String, callback: () -> Unit) = Platform.runLater {
        this.toolBar.items.addAll(
            Separator(),
            Button(text).also {
                it.onAction = EventHandler { callback() }
            }
        )
    }

    fun addKeyStrokeListener(keyStroke: KeyStroke, callback: () -> Unit) = Platform.runLater {
        this.keyStrokeListeners[keyStroke] = callback
    }

    fun addTextChangedListener(callback: () -> Unit) = Platform.runLater {
        this.textArea.textProperty().addListener { _ -> callback() }
    }

    fun showTextDialogBox(
        titleText: String,
        bodyText: String,
        callback: (String?) -> Unit
    ) = Platform.runLater {
        val dialog = TextInputDialog().also { dialog ->
            // Set up text
            dialog.title = titleText
            dialog.headerText = bodyText
            // Observe changes to the result
            dialog.resultProperty().addListener { _, _, value: String? -> callback(value) }
        }
        // Non-blocking show
        dialog.show()
    }

    companion object {
        fun main(args: Array<String>) = launch(*args)
    }
}