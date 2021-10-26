package texteditor;

import kotlin.Unit;
import org.jetbrains.annotations.NotNull;
import texteditor.api.EditorPlugin;
import texteditor.api.EditorText;
import texteditor.api.Listeners;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DatePlugin implements EditorPlugin {
    @NotNull
    @Override
    public String getName() {
        return "Date";
    }

    @Override
    public void start(@NotNull Listeners listeners, @NotNull EditorText editorText, @NotNull Locale locale) {
        listeners.addButton("Date", () -> {
            ZonedDateTime dateTime = ZonedDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter
                    .ofLocalizedDateTime(FormatStyle.MEDIUM)
                    .withLocale(locale);
            String formattedDateTime = dtf.format(dateTime);

            editorText.set(editorText.getCaret(), editorText.getCaret(), formattedDateTime);
        });
    }
}