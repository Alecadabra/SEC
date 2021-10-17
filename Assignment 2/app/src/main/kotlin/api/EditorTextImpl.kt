package api

import EditorText
import javafx.scene.control.TextInputControl

class EditorTextImpl(private val textControl: TextInputControl): EditorText {
    override var caret: Int
        get() = textControl.caretPosition
        set(value) {
            textControl.positionCaret(value)
        }

    override var selectionCaret: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    override var selectionAnchor: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    override val length: Int
        get() = TODO("Not yet implemented")

    override fun get(start: Int, stop: Int) {
        TODO("Not yet implemented")
    }

    override fun get(range: IntRange) {
        TODO("Not yet implemented")
    }

    override fun set(start: Int, stop: Int, value: String) {
        TODO("Not yet implemented")
    }

    override fun set(range: IntRange, value: String) {
        TODO("Not yet implemented")
    }
}