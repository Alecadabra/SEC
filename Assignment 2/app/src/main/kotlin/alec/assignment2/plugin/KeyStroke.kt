package alec.assignment2.plugin

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import texteditor.api.FunctionKey

/**
 * Lightweight wrapper for a keystroke combination, like a [KeyEvent] but less tied to JavaFX.
 */
data class KeyStroke(
    val key: KeyCode,
    val shift: Boolean = false,
    val ctrl: Boolean = false,
    val alt: Boolean = false
)

/**
 * The [KeyStroke] representation of this [KeyEvent].
 */
val KeyEvent.asKeyStroke: KeyStroke
    get() = KeyStroke(
        key = code,
        shift = isShiftDown,
        ctrl = isControlDown,
        alt = isAltDown
    )

/**
 * The [KeyStroke] representation of this [FunctionKey].
 */
val FunctionKey.asKeyStroke: KeyStroke
    get() = KeyStroke(
        key = when (this) {
            FunctionKey.F1 -> KeyCode.F1
            FunctionKey.F2 -> KeyCode.F2
            FunctionKey.F3 -> KeyCode.F3
            FunctionKey.F4 -> KeyCode.F4
            FunctionKey.F5 -> KeyCode.F5
            FunctionKey.F6 -> KeyCode.F6
            FunctionKey.F7 -> KeyCode.F7
            FunctionKey.F8 -> KeyCode.F8
            FunctionKey.F9 -> KeyCode.F9
            FunctionKey.F10 -> KeyCode.F10
            FunctionKey.F11 -> KeyCode.F11
            FunctionKey.F12 -> KeyCode.F12
        }
    )