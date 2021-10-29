package alec.assignment2.keymap

import alec.assignment2.plugin.KeyStroke
import java.lang.RuntimeException

/**
 * Wraps the JavaCC parser [KeymapParser], because it's a JavaCC generated parser class which
 * just doesn't map well into the wonderful world of Kotlin.
 *
 * Handles parsing the given `text` parameter into a `Map<KeyStroke, KeymapAction>` via the
 * [parse] function.
 */
class KeymapController(text: String) {
    private val parser = KeymapParser(text.reader())

    /**
     * Parses the `keymap` file into a mapping of [KeyStroke]s to [KeymapAction]s, or throwing
     * a [KeymapException] if the file has syntax errors.
     */
    fun parse(): Map<KeyStroke, KeymapAction> {
        return try {
            KeymapParser.parse()
        } catch (e: ParseException) {
            throw KeymapException(e.message ?: e.toString(), e)
        }
    }
}

/** Describes a JavaCC parsing error thrown from inside [KeymapController]. */
class KeymapException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
