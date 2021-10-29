package alec.assignment2.plugin

import javafx.scene.control.TextInputControl
import texteditor.api.EditorText

/**
 * The implementation of the api interface [EditorText], using a JavaFX [textControl].
 */
class EditorTextImpl(private val textControl: TextInputControl): EditorText {
    override var caret: Int
        get() = textControl.caretPosition
        set(value) = textControl.positionCaret(value)

    override var selectionCaret: Int
        get() = textControl.caretPosition
        set(value) = textControl.selectRange(selectionAnchor, value)

    override var selectionAnchor: Int
        get() = textControl.anchor
        set(value) = textControl.selectRange(value, selectionCaret)

    override val length: Int
        get() = textControl.length

    override fun get(start: Int, stop: Int): String = textControl.getText(start, stop)

    override fun set(start: Int, stop: Int, value: String) = textControl.replaceText(
        start,
        stop,
        value
    )
}