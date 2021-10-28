package texteditor;

import org.jetbrains.annotations.NotNull;
import texteditor.api.EditorPlugin;
import texteditor.api.EditorText;
import texteditor.api.FunctionKey;
import texteditor.api.Listeners;

import java.util.Locale;

public class FindPlugin implements EditorPlugin
{
    @NotNull
    @Override
    public String getName()
    {
        return "Find";
    }

    @Override
    public void start(@NotNull Listeners listeners,
                      @NotNull EditorText editorText,
                      @NotNull Locale locale)
    {
        listeners.addButton("Find", () -> find(listeners, editorText, locale));
        listeners.addFunctionKeyListener(FunctionKey.F3, () -> find(listeners, editorText, locale));
    }

    private void find(Listeners listeners,
                      EditorText editorText,
                      Locale locale)
    {
        listeners.textDialog(this, "Enter a search term",
                (String searchTerm) -> findSearchTerm(searchTerm, listeners, editorText, locale));
    }

    private void findSearchTerm(String searchTerm,
                                Listeners listeners,
                                EditorText editorText,
                                Locale locale)
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
}