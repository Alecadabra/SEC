package alec.assignment2.ui

import alec.assignment2.i18n.Translation
import alec.assignment2.io.FileIO
import alec.assignment2.io.FileIO.Encoding
import alec.assignment2.keymap.*
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

/**
 * The JavaFX UI application.
 */
class UserInterface : Application() {

    // Private fields ------------------------------------------------------------------------------

    // -------------- I18N -------------------------------------------------------------------------
    /** Contains the localised strings to display on the user interface. */
    private val translation by lazy { Translation() }

    // ButtonTypes with l10n
    private val okButtonType: ButtonType get() = ButtonType(
        translation.ok,
        ButtonBar.ButtonData.OK_DONE
    )
    private val cancelButtonType: ButtonType get() = ButtonType(
        translation.cancel,
        ButtonBar.ButtonData.CANCEL_CLOSE
    )

    // -------------- UI elements ------------------------------------------------------------------
    private val textArea = TextArea()
    private val toolBar = ToolBar()

    // -------------- Plugin & script management ---------------------------------------------------
    private val pluginController = PluginController(this)
    private val keyStrokeListeners = mutableMapOf<KeyStroke, () -> Unit>()

    // -------------- File IO ----------------------------------------------------------------------
    private val fileIO by lazy { FileIO(textController) }

    // JavaFX lifecycle overrides ------------------------------------------------------------------

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

        // Set up the scene, final touches and show
        stage.also { stg ->
            stg.title = "Text Editor"
            stg.minWidth = 800.0

            stg.scene = Scene(
                BorderPane().also { pane ->
                    pane.top = toolBar
                    pane.center = textArea
                }
            ).also { scene ->
                // The key press listener
                scene.setOnKeyPressed { keyEvent ->
                    // This maps keystrokes to callbacks
                    this.keyStrokeListeners[keyEvent.asKeyStroke]?.invoke()
                }
            }

            stg.sizeToScene()
            stg.show()
        }
    }

    // Public API ----------------------------------------------------------------------------------

    /** A controller for the text in the text area */
    val textController: TextInputControl
        get() = this.textArea

    /**
     * Adds a button with the given [text] to the UI with the given [callback].
     *
     * (Deferred call to the UI thread).
     */
    fun addButton(text: String, callback: () -> Unit) = Platform.runLater {
        this.toolBar.items.addAll(
            Separator(),
            Button(text).also {
                it.onAction = EventHandler { callback() }
            }
        )
    }

    /**
     * Adds a listener for a given [keyStroke] to the app with a given [callback].
     *
     * (Deferred call to the UI thread).
     */
    fun addKeyStrokeListener(keyStroke: KeyStroke, callback: () -> Unit) = Platform.runLater {
        this.keyStrokeListeners[keyStroke] = callback
    }

    /**
     * Adds a listener to changes to the text with the given [callback].
     *
     * (Deferred call to the UI thread).
     */
    fun addTextChangedListener(callback: () -> Unit) = Platform.runLater {
        this.textArea.textProperty().addListener { _ -> callback() }
    }

    /**
     * Shows a text input dialog box to the user to request text with a given [titleText] and
     * [bodyText]. The result is sent to [callback], with a String if OK was pressed, or null
     * otherwise.
     *
     * (Deferred call to the UI thread).
     */
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

    // Private dialogs and such --------------------------------------------------------------------

    /** Load the keymap file, parse the DSL, set the keystroke listeners. */
    private fun loadKeymap() = try {
        // Read in the file and pass to the parser to get a Map<KeyStroke, KeymapAction>
        KeymapController(File("../keymap").readText()).parse()
    } catch (e: IOException) {
        Alert(Alert.AlertType.ERROR, e.message, this.okButtonType).showAndWait()
        null
    } catch (e: KeymapException) {
        Alert(Alert.AlertType.ERROR, e.message, this.okButtonType).showAndWait()
        null
    }?.onEach { (keyStroke: KeyStroke, action: KeymapAction) ->
        keyStrokeListeners[keyStroke] = {
            // Helper function to get the string from the start of the current line to the caret
            fun TextArea.currentLine() = this.getText(0, this.caretPosition).let { text ->
                text.replaceBeforeLast(
                    "\n",
                    "",
                    "\n$text"
                )
            }.let { line ->
                // Cut off the \n on the front
                line.slice(1 until line.length)
            }

            when (action.action) {
                KeymapTextAction.INSERT -> when(action.position) {
                    KeymapPositionAction.AT_START -> {
                        val line = textArea.currentLine()
                        val inserted = "${action.text}$line"

                        textArea.replaceText(
                            textArea.caretPosition - line.length,
                            textArea.caretPosition,
                            inserted
                        )
                    }
                    KeymapPositionAction.AT_CARET -> {
                        textArea.insertText(textArea.caretPosition, action.text)
                    }
                }
                KeymapTextAction.DELETE -> {
                    val line = textArea.currentLine()
                    val valid = when (action.position) {
                        KeymapPositionAction.AT_START -> line.startsWith(action.text)
                        KeymapPositionAction.AT_CARET -> line.endsWith(action.text)
                    }
                    if (valid) {
                        val deleted = when (action.position) {
                            KeymapPositionAction.AT_START -> line.replaceFirst(
                                action.text,
                                ""
                            )
                            KeymapPositionAction.AT_CARET -> line.reversed().replaceFirst(
                                action.text.reversed(),
                                ""
                            ).reversed()
                        }

                        textArea.replaceText(
                            textArea.caretPosition - line.length,
                            textArea.caretPosition,
                            deleted
                        )
                    }
                }
            }
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
                            fileChooser.initialDirectory = File("../")
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
            when (buttonType?.buttonData) {
                ButtonBar.ButtonData.OK_DONE -> pluginList
                else -> null
            }
        }
    }.showAndWait().orElse(listOf())!!

    /**
     * Gets the user to select a file encoding setting.
     * @return The selected [Encoding], or null if cancelled.
     */
    private fun selectEncodingDialog(): Encoding? = Dialog<Encoding>().also { dialog ->
        // The dropdown
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

    /**
     * Takes the text from the [textArea], has the user select a file to save as on top of the
     * given [window], and saves using [fileIO].
     */
    private fun saveFile(window: Window) {
        selectEncodingDialog()?.let { encoding ->
            FileChooser().let { fileChooser ->
                fileChooser.initialDirectory = File("../")
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

    /**
     * Replaces the text from the [textArea] with the contents of a file selected on top of the
     * given [window], using [fileIO].
     */
    private fun loadFile(window: Window) {
        selectEncodingDialog()?.also { encoding ->
            FileChooser().let { fileChooser ->
                fileChooser.initialDirectory = File("../")
                fileChooser.title = translation.fileLoad
                fileChooser.extensionFilters.add(
                    FileChooser.ExtensionFilter(translation.filesAll, "*.*")
                )
                fileChooser.showOpenDialog(window)
            }?.also { file ->
                try {
                    this.fileIO.load(file.absolutePath, encoding)
                } catch (e: IOException) {
                    Alert(
                        Alert.AlertType.ERROR, e.message, this.okButtonType
                    ).showAndWait()
                }
            }
        }
    }

    // Main method if ever needed ------------------------------------------------------------------

    companion object {
        fun main(args: Array<String>) = launch(*args)
    }
}