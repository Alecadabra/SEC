package texteditor;

import org.jetbrains.annotations.NotNull;
import texteditor.api.EditorPlugin;
import texteditor.api.EditorText;
import texteditor.api.FunctionKey;
import texteditor.api.Listeners;

import java.util.Locale;
import java.util.ResourceBundle;

public class FindPlugin implements EditorPlugin
{
    private final ResourceBundle strings = ResourceBundle.getBundle("find_strings");
    private final String name = this.strings.getString("find");

    @NotNull
    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public void start(@NotNull Listeners listeners, @NotNull EditorText editorText)
    {
        listeners.addButton(this.name, () -> find(listeners, editorText));
        listeners.addFunctionKeyListener(FunctionKey.F3, () -> find(listeners, editorText));
    }

    private void find(Listeners listeners, EditorText editorText)
    {
        listeners.textDialog(this, this.strings.getString("enter_search_term"),
                (String searchTerm) -> findSearchTerm(searchTerm, listeners, editorText));
    }

    private void findSearchTerm(String searchTerm, Listeners listeners, EditorText editorText)
    {
        String area = editorText.get(editorText.getCaret(), editorText.getLength());
        int areaIdx = area.indexOf(searchTerm);
        if(areaIdx != -1)
        {
            int fullIdx = editorText.getCaret() + areaIdx;
            editorText.setSelectionCaret(fullIdx);
            editorText.setSelectionAnchor(fullIdx + searchTerm.length());
        }
    }

    // Some main method is required for gradle to not complain
    public static void main(String[] args) {}
}