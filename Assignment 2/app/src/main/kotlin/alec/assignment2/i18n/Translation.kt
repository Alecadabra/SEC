package alec.assignment2.i18n

import java.util.Locale

/**
 * Handles translations of strings using a sealed class, where each implementation is a valid
 * translation that maps to a locale.
 */
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
        /**
         * Nice syntactic sugar to get the correct implementation of this class for a [locale].
         * E.g. `Translation(Locale.forLanguageTag("en-AU"))` gives `EnglishAustralia`.
         */
        operator fun invoke(locale: Locale = Locale.getDefault()) = values.singleOrNull {
            it.locale == locale
        } ?: DEFAULT

        /** The fallback Translation. */
        private val DEFAULT = EnglishAustralia

        /** All implemented Translations. */
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

    // This is your thanks for extending the assignment deadline <3
    object EnglishPirate : Translation(Locale.forLanguageTag("en-PT")) {
        override val appName: String = "Text editarr"
        override val loadPluginsAndScripts: String = "Load yer snakes and planks"
        override val loadScript: String = "Load snake"
        override val loadPlugin: String = "Load plank"
        override val scriptLocationInput: String = "Select plank to load"
        override val pluginNameInput: String = "What's the treasure map to yer plank"
        override val filesPython: String = "Snake scrolls"
        override val filesAll: String = "All yer scrolls"
        override val ok: String = "Yarr!"
        override val cancel: String = "Nay"
        override val fileIO: String = "Scroll chests"
        override val fileSelectEncoding: String =
            "How many brass details do you want on the chest (Unnecessary Timber Furniture)"
        override val fileSave: String = "Save yer scroll to a chest"
        override val fileLoad: String = "Load a scroll from a chest"
        override val pluginLoaderClassNotFound: (String) -> String = {
            "Aaarrrgh, plank '$it' was not at the X marks the spot!"
        }
        override val pluginLoaderNoConstructor: (String) -> String = {
            "Aaarrrgh, plank '$it' is in a locked chest!"
        }
        override val pluginLoaderNotPlugin: (String) -> String = {
            "Aaarrrgh, '$it' isn't even a plank!"
        }
        override val pluginLoaderSyntax: (String) -> String = {
            "Aaarrrgh, the snake bit me: \n $it"
        }
    }
}
