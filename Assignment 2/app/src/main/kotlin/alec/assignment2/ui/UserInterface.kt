package alec.assignment2.ui

import alec.assignment2.i18n.LocaleManager
import alec.assignment2.i18n.Translation
import alec.assignment2.plugin.KeyStroke
import alec.assignment2.plugin.PluginController
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
import javafx.stage.Stage
import texteditor.api.EditorPlugin
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

    // UI Elements
    private val textArea = TextArea()
    private val toolBar = ToolBar()

    // Plugins & Listeners
    private val pluginController = PluginController(this)
    private val plugins = listOf<EditorPlugin>()
    private val keyStrokeListeners = mutableMapOf<KeyStroke, () -> Unit>()
    private val textChangedListeners = mutableSetOf<() -> Unit>()

    // Access to text area
    val textController: TextInputControl
        get() = this.textArea

    override fun start(stage: Stage) {
        // Initialise i18n backing properties
        this._locale = LocaleManager.parseLocale(parameters.named)
        this._translation = Translation[locale]

        stage.title = "Text Editor"
        stage.minWidth = 800.0

        // Setup toolbar
        val btn1 = Button("Button1")
        val btn2 = Button("Button2")
        val btn3 = Button("Button3")
        this.toolBar.items.addAll(btn1, btn2, btn3)

        // Subtle user experience tweaks
        toolBar.isFocusTraversable = false
        toolBar.items.forEach { btn: Node -> btn.isFocusTraversable = false }
        textArea.style = "-fx-font-family: 'monospace'" // Set the font

        // Add the main parts of the UI to the window.
        val scene = Scene(
            BorderPane().also {
                it.top = toolBar
                it.center = textArea
            }
        )

        // Load plugins

        // Button event handlers.
        btn1.onAction = EventHandler { showDialog1() }
        btn2.onAction = EventHandler { showDialog2() }
        btn3.onAction = EventHandler { toolBar.items.add(Button("ButtonN")) }

        // TextArea event handlers & caret positioning.
        textArea.textProperty()
            .addListener { _, _, newValue: String ->
                println(
                    """
                    caret position is ${textArea.caretPosition}; text is
                    ---
                    $newValue
                    ---
                    
                    """.trimIndent())
            }
        textArea.text =
            "This is some\ndemonstration text\nTry pressing F1, ctrl+b, ctrl+shift+b or alt+b."
        textArea.selectRange(8, 16) // Select a range of text (and move the caret to the end)

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

    private fun showDialog1() {
        // TextInputDialog is a subclass of Dialog that just presents a single text field.
        val dialog = TextInputDialog()
        dialog.title = "Text entry dialog box"
        dialog.headerText = "Enter text"

        // 'showAndWait()' opens the dialog and waits for the user to press the 'OK' or 'Cancel' button. It returns an Optional, which is a whole other discussion, but we can call 'orElse(null)' on that to get the actual string entered if the user pressed 'OK', or null if the user pressed 'Cancel'.
        val inputStr = dialog.showAndWait().orElse(null)
        if (inputStr != null) {
            // Alert is another specialised dialog just for displaying a quick message.
            Alert(
                Alert.AlertType.INFORMATION,
                "You entered '$inputStr'",
                ButtonType.OK).showAndWait()
        }
    }

    private fun showDialog2() {
        val addBtn = Button("Add...")
        val removeBtn = Button("Remove...")
        val toolBar = ToolBar(addBtn, removeBtn)
        addBtn.onAction = EventHandler {
            Alert(
                Alert.AlertType.INFORMATION,
                "Add...",
                ButtonType.OK
            ).showAndWait()
        }
        removeBtn.onAction = EventHandler {
            Alert(
                Alert.AlertType.INFORMATION,
                "Remove...",
                ButtonType.OK
            ).showAndWait()
        }

        // FYI: 'ObservableList' inherits from the ordinary List interface, but also works as a subject for any 'observer-pattern' purposes; e.g., to allow an on-screen ListView to display any changes made to the list as they are made.
        val list = FXCollections.observableArrayList<String>()
        val listView = ListView(list)
        list.add("Item 1")
        list.add("Item 2")
        list.add("Item 3")
        val box = BorderPane()
        box.top = toolBar
        box.center = listView
        val dialog: Dialog<*> = Dialog<Any?>()
        dialog.title = "List of things"
        dialog.headerText = "Here's a list of things"
        dialog.dialogPane.content = box
        dialog.dialogPane.buttonTypes.add(ButtonType.OK)
        dialog.showAndWait()
    }

    fun addButton(text: String, callback: () -> Unit) = Platform.runLater {
        this.toolBar.items.add(
            Button(text).also {
                it.onAction = EventHandler { callback() }
            }
        )
    }

    fun addKeyStrokeListener(keyStroke: KeyStroke, callback: () -> Unit) = Platform.runLater {
        this.keyStrokeListeners[keyStroke] = callback
    }

    fun addTextChangedListener(callback: () -> Unit) = Platform.runLater {
        this.textChangedListeners.add(callback)
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