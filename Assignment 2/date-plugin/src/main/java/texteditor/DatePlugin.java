package texteditor;

import org.jetbrains.annotations.NotNull;
import texteditor.api.EditorPlugin;
import texteditor.api.EditorText;
import texteditor.api.Listeners;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class DatePlugin implements EditorPlugin
{
    @NotNull
    @Override
    public String getName()
    {
        return "Date";
    }

    @Override
    public void start(@NotNull Listeners listeners,
                      @NotNull EditorText editorText,
                      @NotNull Locale locale)
    {
        ResourceBundle strings = ResourceBundle.getBundle("strings", locale);
        String name = strings.getString("date");
        listeners.addButton(name, () ->
        {
            ZonedDateTime dateTime = ZonedDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter
                    .ofLocalizedDateTime(FormatStyle.MEDIUM)
                    .withLocale(locale);
            String formattedDateTime = dtf.format(dateTime);

            editorText.set(editorText.getCaret(), editorText.getCaret(), formattedDateTime);
        });
    }
}