package texteditor.api

import java.util.function.Consumer

interface Listeners {
    fun addButton(text: String, callback: () -> Unit)
    // Java interoperability
    fun addButton(text: String, runnable: Runnable) = addButton(text, runnable::run)

    fun addFunctionKeyListener(functionKey: FunctionKey, callback: () -> Unit)
    // Java interoperability
    fun addFunctionKeyListener(functionKey: FunctionKey, runnable: Runnable) = addFunctionKeyListener(
        functionKey,
        runnable::run
    )

    fun addTextChangedListener(callback: () -> Unit)
    // Java interoperability
    fun addTextChangedListener(runnable: Runnable) = addTextChangedListener(runnable::run)

    fun textDialog(plugin: EditorPlugin, prompt: String, callback: (String?) -> Unit)
    // Java interoperability
    fun textDialog(plugin: EditorPlugin, prompt: String, consumer: Consumer<String?>) = textDialog(
        plugin,
        prompt
    ) { consumer.accept(it) }
}

// Extension functions for Java interoperability
