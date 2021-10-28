package alec.assignment2.plugin

import alec.assignment2.i18n.Translation
import org.python.core.PyException
import org.python.core.PySyntaxError
import org.python.util.PythonInterpreter
import texteditor.api.EditorPlugin
import java.io.File
import java.lang.RuntimeException

class PluginLoader(private val translation: Translation) {
    /**
     * Loads a python script from the given [path].
     *
     * @throws PluginLoaderException If an error is encountered.
     */
    fun loadScript(path: String): EditorPlugin {
        // Resolve the name of the python class from the file name, `src/emoji.py` becomes `Emoji`
        val pyClassName = File(path).let { file ->
            file.name.substring(0, file.name.length - file.extension.length - 1).replaceFirstChar {
                it.titlecase()
            }
        }

        val plugin = PythonInterpreter().use { py ->
            // Execute the file containing the script
            try {
                py.execfile(path)
            } catch (e: PySyntaxError) {
                throw PluginLoaderException(translation.pluginLoaderSyntax(e.toString()), e)
            }

            // Instantiate the class
            val pyObj = try {
                py.eval("$pyClassName()")
            } catch (e: PyException) {
                throw PluginLoaderException(translation.pluginLoaderSyntax(e.toString()), e)
            }

            // Convert the python object to an EditorPlugin
            return@use try {
                pyObj.__tojava__(EditorPlugin::class.java) as EditorPlugin
            } catch (e: PyException) {
                throw PluginLoaderException(translation.pluginLoaderNotPlugin(pyClassName), e)
            }
        }

        return plugin
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

class PluginLoaderException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)