import java.util.*

interface EditorPlugin {
    /**
     * The human-readable name of this plugin to display to the user.
     */
    val name: String

    /**
     * Called to start the plugin and register all actions.
     */
    fun start(listeners: Listeners, editorText: EditorText, locale: Locale)
}