package alec.assignment2.io

import javafx.scene.control.TextInputControl
import java.io.File
import java.nio.charset.Charset

/**
 * Handles the loading and saving of the text of a given [textControl] using its own file charset
 * specification [Encoding].
 */
class FileIO(private val textControl: TextInputControl) {

    /**
     * Saves the contents of [textControl] to a given file [path] using a specified [encoding].
     */
    fun save(path: String, encoding: Encoding) = File(path).writer(encoding.charset).use {
        it.write(textControl.text)
    }

    /**
     * Loads into the contents of [textControl] the contents of the file at the given [path] using
     * a specified [encoding].
     */
    fun load(path: String, encoding: Encoding) {
        textControl.text = File(path).reader(encoding.charset).use {
            it.readText()
        }
    }

    /**
     * The supported file encodings/charsets for [FileIO].
     *
     * Each has a direct mapping to a supported [Charset].
     */
    enum class Encoding(val charset: Charset) {
        UTF_8(Charsets.UTF_8),
        UTF_16(Charsets.UTF_16),
        UTF_32(Charsets.UTF_32);

        /** The canonical name of the underlying [Charset], i.e. `UTF-8`. */
        override fun toString(): String = charset.name()
    }
}