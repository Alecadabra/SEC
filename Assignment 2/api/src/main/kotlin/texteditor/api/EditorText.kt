package texteditor.api

interface EditorText {
    var caret: Int

    var selectionCaret: Int
    var selectionAnchor: Int

    val length: Int

    operator fun get(start: Int, stop: Int = start + 1): String

    operator fun set(start: Int, stop: Int = start + 1, value: String)
}

val EditorText.lastIndex: Int
    get() = length - 1

operator fun EditorText.get(range: IntRange) = get(range.first, range.last)

operator fun EditorText.set(range: IntRange, value: String) = set(range.first, range.last, value)