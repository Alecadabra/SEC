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
        this.ui.addKeyStrokeListener(functionKey.asKeyStroke, callback)
    }

    override fun addTextChangedListener(callback: () -> Unit) {
        this.ui.addTextChangedListener(callback)
    }

    override fun textDialog(plugin: EditorPlugin, prompt: String, callback: (String?) -> Unit) {
        this.ui.showTextDialogBox(plugin.name, prompt, callback)
    }

    fun startPlugin(plugin: EditorPlugin) = plugin.start(this, this.editorText)
}