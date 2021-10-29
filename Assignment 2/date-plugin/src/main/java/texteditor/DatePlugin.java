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
    private final String name = ResourceBundle.getBundle("date_strings").getString("date");

    @NotNull
    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public void start(@NotNull Listeners listeners,
                      @NotNull EditorText editorText)
    {
        listeners.addButton(this.name, () ->
        {
            ZonedDateTime dateTime = ZonedDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter
                    .ofLocalizedDateTime(FormatStyle.MEDIUM)
                    .withLocale(Locale.getDefault());
            String formattedDateTime = dtf.format(dateTime);

            editorText.set(editorText.getCaret(), editorText.getCaret(), formattedDateTime);
        });
    }

    // Some main method is required for gradle to not complain
    public static void main(String[] args) {}
}