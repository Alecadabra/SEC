package texteditor.api

import java.util.*

/**
 * The interface to implement when making a plugin.
 */
interface EditorPlugin {
    /**
     * The human-readable name of this plugin to display to the user.
     */
    val name: String

    /**
     * Called to start the plugin and register all actions.
     */
    fun start(listeners: Listeners, editorText: EditorText)
}