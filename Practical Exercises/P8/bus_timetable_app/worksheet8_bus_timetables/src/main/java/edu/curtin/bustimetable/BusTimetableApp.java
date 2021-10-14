package edu.curtin.bustimetable;

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
        if(localeString != null)
        {
            // FIXME: A locale was specified on the command-line. What are you going to do about it? ;-)
        }
    
        var entries = FXCollections.<TimetableEntry>observableArrayList();
        FileIO fileIO = new FileIO();
        LoadSaveUI loadSaveUI = new LoadSaveUI(stage, entries, fileIO);
        AddUI addUI = new AddUI(entries);
        new MainUI(stage, entries, loadSaveUI, addUI).display();
    }
}
