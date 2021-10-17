package edu.curtin.bustimetable;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;


/**
 * Entry point for the bus timetabling app. It can be invoked with the command-line parameter 
 * --locale=[value].
 */
public class BusTimetableApp extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }
        
    @Override
    public void start(Stage stage)
    {
        var localeString = getParameters().getNamed().get("locale");
        ResourceBundle strings = getStrings(localeString);
    
        var entries = FXCollections.<TimetableEntry>observableArrayList();
        FileIO fileIO = new FileIO(strings);
        LoadSaveUI loadSaveUI = new LoadSaveUI(stage, entries, fileIO, strings);
        AddUI addUI = new AddUI(entries, strings);
        new MainUI(stage, entries, loadSaveUI, addUI, strings).display();
    }

    /**
     * Gets the resource bundle for translations, from the possibly-null
     * command line parameter --locale=[value].
     * Prints out messages about the parsed value.
     */
    private ResourceBundle getStrings(String localeString)
    {
        ResourceBundle strings;
        if(localeString == null)
        {
            Locale locale = Locale.getDefault();
            strings = ResourceBundle.getBundle("strings", locale);

            if (strings.getLocale().equals(Locale.getDefault()))
            {
                System.out.println("Loading locale '" + strings.getLocale() + "' (default)");
            }
            else
            {
                System.out.println("Loading locale '" + strings.getLocale() + "' (no match for default locale '" + locale + "')");
            }
        }
        else
        {
            Locale locale = Locale.forLanguageTag(localeString);
            strings = ResourceBundle.getBundle("strings", locale);

            if (strings.getLocale().equals(Locale.getDefault()))
            {
                if (strings.getLocale().equals(locale))
                {
                    System.out.println("Loading locale '" + strings.getLocale() + "' (default)");
                }
                else
                {
                    System.out.println("Loading locale '" + strings.getLocale() + "' (default) (no match for command-line input '" + localeString + "')");
                }
            }
            else
            {
                System.out.println("Loading locale '" + strings.getLocale() + "' from command-line input");
            }
        }

        return strings;
    }
}
