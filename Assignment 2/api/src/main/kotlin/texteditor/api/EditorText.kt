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
     * Set the text string from [start] (inclusive) to [stop] (exclusive) to [value].
     */
    operator fun set(start: Int, stop: Int = start + 1, value: String)
}

// Extension members for ease of use

/**
 * The last valid index of the text.
 */
val EditorText.lastIndex: Int
    get() = length - 1

/**
 * Get the text string from the range of indices in [range].
 */
operator fun EditorText.get(range: IntRange) = get(range.first, range.last)

/**
 * Set the text string from the range of indices in [range] to [value].
 */
operator fun EditorText.set(range: IntRange, value: String) = set(range.first, range.last, value)
