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

    abstract val fileIO: String
    abstract val fileSelectEncoding: String
    abstract val fileSave: String
    abstract val fileLoad: String

    abstract val pluginLoaderClassNotFound: (String) -> String
    abstract val pluginLoaderNoConstructor: (String) -> String
    abstract val pluginLoaderNotPlugin: (String) -> String
    abstract val pluginLoaderSyntax: (String) -> String

    companion object {
        operator fun invoke(locale: Locale = Locale.getDefault()) = values.singleOrNull {
            it.locale == locale
        } ?: DEFAULT

        private val DEFAULT = EnglishAustralia

        private val values = listOf(EnglishAustralia, EnglishPirate)
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
        override val fileIO: String = "File IO"
        override val fileSelectEncoding: String = "Select file encoding"
        override val fileSave: String = "Save"
        override val fileLoad: String = "Load"
        override val pluginLoaderClassNotFound: (String) -> String = {
            "Class '$it' not found"
        }
        override val pluginLoaderNoConstructor: (String) -> String = {
            "Class '$it' has no zero-argument constructor"
        }
        override val pluginLoaderNotPlugin: (String) -> String = {
            "Class '$it' is does not inherit from EditorPlugin"
        }
        override val pluginLoaderSyntax: (String) -> String = {
            "Syntax error in file: \n $it"
        }
    }

    // Sorry! Didn't have time to write anything special for these :'(
    object EnglishPirate : Translation(Locale.forLanguageTag("en-PT")) {
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
        override val fileIO: String = "File IO"
        override val fileSelectEncoding: String = "Select file encoding"
        override val fileSave: String = "Save"
        override val fileLoad: String = "Load"
        override val pluginLoaderClassNotFound: (String) -> String = {
            "Class '$it' not found"
        }
        override val pluginLoaderNoConstructor: (String) -> String = {
            "Class '$it' has no zero-argument constructor"
        }
        override val pluginLoaderNotPlugin: (String) -> String = {
            "Class '$it' is does not inherit from EditorPlugin"
        }
        override val pluginLoaderSyntax: (String) -> String = {
            "Syntax error in file: \n $it"
        }
    }
}
