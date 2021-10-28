package texteditor.api

import java.util.function.Consumer

/**
 * A plugin's access to the UI listener systems.
 */
interface Listeners {
    /**
     * Add a text button to the UI with given [text] and [callback].
     */
    fun addButton(text: String, callback: () -> Unit)
    // Java interoperability
    fun addButton(text: String, runnable: Runnable) = addButton(text, runnable::run)

    /**
     * Add a [functionKey] (Eg. [FunctionKey.F3]) listener with a set [callback].
     */
    fun addFunctionKeyListener(functionKey: FunctionKey, callback: () -> Unit)
    // Java interoperability
    fun addFunctionKeyListener(functionKey: FunctionKey, runnable: Runnable) = addFunctionKeyListener(
        functionKey,
        runnable::run
    )

    /**
     * Add a [callback] for every change to the text area.
     */
    fun addTextChangedListener(callback: () -> Unit)
    // Java interoperability
    fun addTextChangedListener(runnable: Runnable) = addTextChangedListener(runnable::run)

    /**
     * Send a text dialog to the user from your [plugin] with a [prompt] string.
     * The [callback] receives null if the dialog was cancelled, and a string otherwise.
     */
    fun textDialog(plugin: EditorPlugin, prompt: String, callback: (String?) -> Unit)
    // Java interoperability
    fun textDialog(plugin: EditorPlugin, prompt: String, consumer: Consumer<String?>) = textDialog(
        plugin,
        prompt
    ) { consumer.accept(it) }
}
