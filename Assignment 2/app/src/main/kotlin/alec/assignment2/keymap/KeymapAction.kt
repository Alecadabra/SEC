package alec.assignment2.keymap

import alec.assignment2.plugin.KeyStroke

data class KeymapAction(
    val action: KeymapTextAction,
    val text: String,
    val position: KeymapPositionAction
)

enum class KeymapTextAction {
    INSERT,
    DELETE
}

enum class KeymapPositionAction {
    AT_START,
    AT_CARET
}