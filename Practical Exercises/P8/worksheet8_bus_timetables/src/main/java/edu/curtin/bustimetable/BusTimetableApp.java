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
        Locale locale;
        if(localeString == null)
        {
            locale = Locale.getDefault();
        }
        else
        {
            locale = Locale.forLanguageTag(localeString);
        }
        System.out.println("Localestring: " + localeString + ", locale: " + locale);

        ResourceBundle strings = ResourceBundle.getBundle("strings", locale);
    
        var entries = FXCollections.<TimetableEntry>observableArrayList();
        FileIO fileIO = new FileIO(strings);
        LoadSaveUI loadSaveUI = new LoadSaveUI(stage, entries, fileIO, strings);
        AddUI addUI = new AddUI(entries, strings);
        new MainUI(stage, entries, loadSaveUI, addUI, strings).display();
    }
}
