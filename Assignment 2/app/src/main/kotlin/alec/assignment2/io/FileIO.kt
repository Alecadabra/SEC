package alec.assignment2.io

import javafx.scene.control.TextInputControl
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.charset.Charset

class FileIO(private val textControl: TextInputControl) {

    fun save(path: String, encoding: Encoding) = File(path).writer(encoding.charset).use {
        it.write(textControl.text)
    }

    fun load(path: String, encoding: Encoding): String = File(path).reader(encoding.charset).use {
        it.readText()
    }

    enum class Encoding(val charset: Charset) {
        UTF_8(Charsets.UTF_8),
        UTF_16(Charsets.UTF_16),
        UTF_32(Charsets.UTF_32);

        override fun toString(): String = charset.name()
    }
}