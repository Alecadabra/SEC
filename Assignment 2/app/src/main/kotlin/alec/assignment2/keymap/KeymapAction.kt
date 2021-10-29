package alec.assignment2.keymap

/** Describes the action taken by a key mapping in the `keymap` file. */
data class KeymapAction(
    val action: KeymapTextAction,
    val text: String,
    val position: KeymapPositionAction
)

/** The text operation performed by a [KeymapAction]. */
enum class KeymapTextAction {
    INSERT,
    DELETE
}

/** The position of the text operation performed by a [KeymapAction]. */
enum class KeymapPositionAction {
    AT_START,
    AT_CARET
}