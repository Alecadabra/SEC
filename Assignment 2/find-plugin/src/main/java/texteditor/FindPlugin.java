package texteditor;

import org.jetbrains.annotations.NotNull;
import texteditor.api.EditorPlugin;
import texteditor.api.EditorText;
import texteditor.api.FunctionKey;
import texteditor.api.Listeners;

import java.text.Normalizer;
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
        // Get area after caret
        String area = editorText.get(editorText.getCaret(), editorText.getLength());

        // Normalise
        String normalSearchTerm = Normalizer.normalize(searchTerm, Normalizer.Form.NFKC);
        String normalArea = Normalizer.normalize(area, Normalizer.Form.NFKC);

        // Search
        int areaIdx = normalArea.indexOf(normalSearchTerm);
        if(areaIdx != -1)
        {
            int fullIdx = Math.min(
                    editorText.getCaret() + areaIdx,
                    editorText.getLength() - 1);
            editorText.setSelectionCaret(fullIdx);
            editorText.setSelectionAnchor(fullIdx + searchTerm.length());
        }
    }

    // Some main method is required for gradle to not complain
    public static void main(String[] args) {}
}