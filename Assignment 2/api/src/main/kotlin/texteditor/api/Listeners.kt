package texteditor.api

interface Listeners {
    fun addButton(text: String, callback: () -> Unit)

    fun addFunctionKeyListener(functionKey: FunctionKey, callback: () -> Unit)

    fun addTextChangedListener(callback: () -> Unit)

    fun textDialog(plugin: EditorPlugin, prompt: String, callback: (String?) -> Unit)
}