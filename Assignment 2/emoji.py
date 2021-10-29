from texteditor.api import EditorPlugin
from java.util import Locale
from collections import defaultdict

# A Jython script to demonstrate the plugin/scripting API of the app.

# Helper function
def _replaceEmoji(editorText):
    a, b = editorText.caret - 3, editorText.caret

    # Bounds check
    if a >= 0 and b < editorText.length:
        try:
            if editorText.get(a, b) == ':-)':
                editorText.set(a, b, u'\U0001f60a')
        except:
            # Not a match - do nothing
            pass

# Maps locale language tags to the corresponding reported script name
names = defaultdict(
    lambda: 'Emoji',
    {
        'en-AU': 'Emoji',
        'en-PT': 'Jolly Rogers'
    }
)

# The script implementation
class Emoji(EditorPlugin):
    '''
    A text editor plugin to replace typed strings ':-)' with a smiley emoji.
    '''

    def getName(self):
        return names[Locale.getDefault().toLanguageTag()]

    def start(self, listeners, editorText):
        listeners.addTextChangedListener(lambda: _replaceEmoji(editorText))
