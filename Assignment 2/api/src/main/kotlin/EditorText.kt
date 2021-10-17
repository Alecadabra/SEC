interface EditorText {
    var caret: Int

    var selectionCaret: Int
    var selectionAnchor: Int

    val length: Int
    val lastIndex: Int
        get() = length - 1

    operator fun get(start: Int, stop: Int = start + 1)
    operator fun get(range: IntRange)

    operator fun set(start: Int, stop: Int = start + 1, value: String)
    operator fun set(range: IntRange, value: String)
}