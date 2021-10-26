package alec.assignment2.plugin

import alec.assignment2.i18n.Translation
import texteditor.api.EditorPlugin
import java.lang.RuntimeException

class PluginLoader(private val translation: Translation) {
    /**
     * Loads a python script from the given [path].
     *
     * @throws PluginLoaderException If an error is encountered.
     */
    fun loadScript(path: String): EditorPlugin {
        throw PluginLoaderException("Implement script loading")
    }

    /**
     * Loads a Java plugin from the given fully qualified [className].
     *
     * @throws PluginLoaderException If an error is encountered.
     */
    fun loadPlugin(className: String): EditorPlugin {
        val cls: Class<*> = try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw PluginLoaderException(translation.pluginLoaderClassNotFound(className), e)
        }

        val instance: Any = try {
            cls.getConstructor().newInstance()
        } catch (e: NoSuchMethodException) {
            throw PluginLoaderException(translation.pluginLoaderNoConstructor(cls.simpleName), e)
        }

        val plugin: EditorPlugin = try {
            instance as EditorPlugin
        } catch (e: TypeCastException) {
            throw PluginLoaderException(translation.pluginLoaderNotPlugin(cls.simpleName), e)
        }

        return plugin
    }
}

class PluginLoaderException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)