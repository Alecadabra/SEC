package texteditor.api

/**
 * Gives access to the text area of the UI.
 */
interface EditorText {
    /** The current position index of the caret */
    var caret: Int

    /**
     * The current position index of the caret of selection.
     *
     * Set this to select text.
     */
    var selectionCaret: Int
    /**
     * The current position index of the anchor of selection.
     *
     * Set this to select text.
     */
    var selectionAnchor: Int

    /**
     * The size of the text.
     */
    val length: Int

    /**
     * Get the text string from [start] (inclusive) to [stop] (exclusive)
     */
    operator fun get(start: Int, stop: Int = start + 1): String

    /**
     * Set the text string from [start] (inclusive) to [stop] (exclusive)
     */
    operator fun set(start: Int, stop: Int = start + 1, value: String)
}

// Extension members for ease of use

val EditorText.lastIndex: Int
    get() = length - 1

operator fun EditorText.get(range: IntRange) = get(range.first, range.last)

operator fun EditorText.set(range: IntRange, value: String) = set(range.first, range.last, value)