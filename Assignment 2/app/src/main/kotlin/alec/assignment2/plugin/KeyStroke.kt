package alec.assignment2.plugin

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

/**
 * Lightweight wrapper for a keystroke combination, like a [KeyEvent] but less tied to JavaFX.
 */
data class KeyStroke(
    val key: KeyCode,
    val shift: Boolean = false,
    val ctrl: Boolean = false,
    val alt: Boolean = false
)

fun KeyStroke.matches(keyEvent: KeyEvent): Boolean {
    val matchShift = shift == keyEvent.isShiftDown
    val matchCtrl = ctrl == keyEvent.isControlDown
    val matchAlt = alt == keyEvent.isAltDown
    val matchKey = key == keyEvent.code

    return matchShift && matchCtrl && matchAlt && matchKey
}