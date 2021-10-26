package alec.assignment2.ui

import alec.assignment2.i18n.LocaleManager
import alec.assignment2.i18n.Translation
import alec.assignment2.plugin.*
import javafx.application.Application
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.stage.Window
import texteditor.api.EditorPlugin
import java.io.File
import java.util.*

class UserInterface : Application() {

    // I18N
    /**
     * The locale for this application.
     *
     * Can only be accessed after application initialisation.
     */
    val locale: Locale
        get() = _locale ?: error("Locale accessed before application initialised")
    private var _locale: Locale? = null

    /**
     * Contains the localised strings to display on the user interface.
     *
     * Can only be accessed after application initialisation.
     */
    private val translation: Translation
        get() = _translation ?: error("Translations accessed before application initialised")
    private var _translation: Translation? = null
    // ButtonTypes with l10n
    private val okButtonType: ButtonType get() = ButtonType(translation.ok, ButtonBar.ButtonData.OK_DONE)
    private val cancelButtonType: ButtonType get() = ButtonType(translation.cancel, ButtonBar.ButtonData.CANCEL_CLOSE)

    // UI Elements
    private val scene by lazy {
        Scene(
            BorderPane().also {
                it.top = toolBar
                it.center = textArea
            }
        )
    }
    private val textArea = TextArea()
    private val toolBar = ToolBar()

    // Plugin & script management
    private val pluginLoader: PluginLoader get() = PluginLoader(translation)
    private val pluginController = PluginController(this)
    private val keyStrokeListeners = mutableMapOf<KeyStroke, () -> Unit>()

    // Access to text area
    val textController: TextInputControl
        get() = this.textArea

    override fun start(stage: Stage) {
        // Initialise i18n backing properties
        this._locale = LocaleManager.parseLocale(parameters.named)
        this._translation = Translation[locale]

        stage.title = "Text Editor"
        stage.minWidth = 800.0

        // Subtle user experience tweaks
        toolBar.isFocusTraversable = false
        toolBar.items.forEach { btn: Node -> btn.isFocusTraversable = false }
        textArea.style = "-fx-font-family: 'monospace'" // Set the font

        // Load plugins
        val plugins = loadPluginsDialog(stage)
        plugins.forEach { plugin ->
            this.pluginController.startPlugin(plugin)
        }

        // Example global keypress handler.
        scene.onKeyPressed = EventHandler { keyEvent: KeyEvent ->
            // See the documentation for the KeyCode class to see all the available keys.
            val key = keyEvent.code
            val ctrl = keyEvent.isControlDown
            val shift = keyEvent.isShiftDown
            val alt = keyEvent.isAltDown
            if (key == KeyCode.F1) {
                Alert(Alert.AlertType.INFORMATION, "F1", ButtonType.OK).showAndWait()
            }
            else if (ctrl && shift && key == KeyCode.B) {
                Alert(Alert.AlertType.INFORMATION, "ctrl+shift+b", ButtonType.OK).showAndWait()
            }
            else if (ctrl && key == KeyCode.B) {
                Alert(Alert.AlertType.INFORMATION, "ctrl+b", ButtonType.OK).showAndWait()
            }
            else if (alt && key == KeyCode.B) {
                Alert(Alert.AlertType.INFORMATION, "alt+b", ButtonType.OK).showAndWait()
            }
        }
        stage.scene = scene
        stage.sizeToScene()
        stage.show()
    }

    private fun loadPluginsDialog(window: Window): List<EditorPlugin> {
        val dialog = Dialog<List<EditorPlugin>>().also { dialog ->
            val pluginList = FXCollections.observableArrayList<EditorPlugin>()

            dialog.title = translation.appName
            dialog.headerText = translation.loadPluginsAndScripts
            dialog.dialogPane.content = BorderPane().also { box ->
                box.top = ToolBar(
                    Button(translation.loadScript).also {
                        it.setOnAction {
                            FileChooser().also { fileChooser ->
                                fileChooser.title = translation.scriptLocationInput
                                fileChooser.extensionFilters.addAll(
                                    FileChooser.ExtensionFilter(translation.filesPython, "*.py"),
                                    FileChooser.ExtensionFilter(translation.filesAll, "*.*")
                                )
                                val file: File? = fileChooser.showOpenDialog(window)
                                if (file != null) {
                                    try {
                                        pluginList.add(this.pluginLoader.loadScript(file.absolutePath))
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
                    Button(translation.loadPlugin).also {
                        it.setOnAction {
                            TextInputDialog().also { textDialog ->
                                textDialog.title = translation.loadPlugin
                                textDialog.headerText = translation.pluginNameInput
                                textDialog.resultProperty().addListener { _, _, value ->
                                    try {
                                        pluginList.add(this.pluginLoader.loadPlugin(value))
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
        }
        // Return new plugins list if OK is pressed
        return dialog.showAndWait().orElse(listOf())
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
        this.scene.setOnKeyPressed { keyEvent ->
            this.keyStrokeListeners[keyEvent.asKeyStroke]?.invoke()
        }
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
            // Return the entered text if OK is pressed, or null if CANCEL is pressed
            dialog.setResultConverter { btnType ->
                when (btnType.buttonData) {
                    ButtonBar.ButtonData.OK_DONE -> dialog.contentText
                    ButtonBar.ButtonData.CANCEL_CLOSE -> null
                    else -> error("Invalid dialog box button")
                }
            }
            // Observe changes to the result
            dialog.resultProperty().addListener { _, _, value: String? ->
                callback(value)
            }
        }
        // Non-blocking show
        dialog.show()
    }

    companion object {
        fun main(args: Array<String>) = launch(*args)
    }
}