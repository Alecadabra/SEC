package alec.assignment2.keymap

import alec.assignment2.plugin.KeyStroke
import java.lang.RuntimeException

class KeymapController(text: String) {
    val parser = KeymapParser(text.reader())

    fun parse(): Map<KeyStroke, KeymapAction> {
        return try {
            KeymapParser.parse()
        } catch (e: ParseException) {
            throw KeymapException(e.message ?: e.toString(), e)
        }
    }
}

class KeymapException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
