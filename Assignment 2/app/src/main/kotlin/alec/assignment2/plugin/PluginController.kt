package alec.assignment2.plugin

import alec.assignment2.ui.UserInterface
import javafx.application.Platform
import javafx.scene.input.KeyCode
import texteditor.api.EditorPlugin
import texteditor.api.EditorText
import texteditor.api.FunctionKey
import texteditor.api.Listeners

class PluginController(private val ui: UserInterface) : Listeners {
    private val editorText: EditorText = EditorTextImpl(this.ui.textController)

    override fun addButton(text: String, callback: () -> Unit) {
        this.ui.addButton(text, callback)
    }

    override fun addFunctionKeyListener(functionKey: FunctionKey, callback: () -> Unit) {
        val keyCode = when (functionKey) {
            FunctionKey.F1 -> KeyCode.F1
            FunctionKey.F2 -> KeyCode.F2
            FunctionKey.F3 -> KeyCode.F3
            FunctionKey.F4 -> KeyCode.F4
            FunctionKey.F5 -> KeyCode.F5
            FunctionKey.F6 -> KeyCode.F6
            FunctionKey.F7 -> KeyCode.F7
            FunctionKey.F8 -> KeyCode.F8
            FunctionKey.F9 -> KeyCode.F9
            FunctionKey.F10 -> KeyCode.F10
            FunctionKey.F11 -> KeyCode.F11
            FunctionKey.F12 -> KeyCode.F12
        }
        val keyStroke = KeyStroke(keyCode)

        this.ui.addKeyStrokeListener(keyStroke, callback)
    }

    override fun addTextChangedListener(callback: () -> Unit) {
        this.ui.addTextChangedListener(callback)
    }

    override fun textDialog(plugin: EditorPlugin, prompt: String, callback: (String?) -> Unit) {
        this.ui.showTextDialogBox(plugin.name, prompt, callback)
    }

    fun startPlugin(plugin: EditorPlugin) {
        plugin.start(this, this.editorText, this.ui.locale)
    }

}