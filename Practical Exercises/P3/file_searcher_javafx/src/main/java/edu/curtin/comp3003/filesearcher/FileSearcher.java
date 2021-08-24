package edu.curtin.comp3003.filesearcher;

import javafx.application.Application;
import javafx.stage.Stage;

public class FileSearcher extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }
    
    @Override
    public void start(Stage stage)
    {
        new FSUserInterface().show(stage);
    }
}
