package texteditor;

import org.jetbrains.annotations.NotNull;
import texteditor.api.EditorPlugin;
import texteditor.api.EditorText;
import texteditor.api.Listeners;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class FindPlugin implements EditorPlugin {
    @NotNull
    @Override
    public String getName() {
        return "Find";
    }

    @Override
    public void start(@NotNull Listeners listeners, @NotNull EditorText editorText, @NotNull Locale locale) {

    }
}