package alec.assignment2.i18n

import java.util.*

sealed class Translation(val locale: Locale) {
    abstract val appName: String

    abstract val loadPluginsAndScripts: String
    abstract val loadScript: String
    abstract val loadPlugin: String
    abstract val scriptLocationInput: String
    abstract val pluginNameInput: String
    abstract val filesPython: String
    abstract val filesAll: String

    abstract val ok: String
    abstract val cancel: String

    abstract val pluginLoaderClassNotFound: (String) -> String
    abstract val pluginLoaderNoConstructor: (String) -> String
    abstract val pluginLoaderNotPlugin: (String) -> String

    companion object {
        operator fun get(locale: Locale): Translation = values.singleOrNull {
            it.locale == locale
        } ?: DEFAULT

        val DEFAULT = EnglishAustralia

        val values = listOf(EnglishAustralia)
    }

    object EnglishAustralia : Translation(Locale.forLanguageTag("en-AU")) {
        override val appName: String = "Text Editor"
        override val loadPluginsAndScripts: String = "Load Plugins and Scripts"
        override val loadScript: String = "Load Script"
        override val loadPlugin: String = "Load Plugin"
        override val scriptLocationInput: String = "Select plugin to load"
        override val pluginNameInput: String = "Provide the fully qualified class name of the plugin"
        override val filesPython: String = "Python Files"
        override val filesAll: String = "All Files"
        override val ok: String = "OK"
        override val cancel: String = "Cancel"
        override val pluginLoaderClassNotFound: (String) -> String = {
            "Class '$it' not found"
        }
        override val pluginLoaderNoConstructor: (String) -> String = {
            "Class '$it' has no zero-argument constructor"
        }
        override val pluginLoaderNotPlugin: (String) -> String = {
            "Class '$it' is does not inherit from EditorPlugin"
        }
    }
}
