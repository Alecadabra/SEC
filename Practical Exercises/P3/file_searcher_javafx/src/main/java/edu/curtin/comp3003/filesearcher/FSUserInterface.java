package edu.curtin.comp3003.filesearcher;

import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * A JavaFX-based user interface for the file searcher.
 */
public class FSUserInterface
{
    private static final double SPACING = 8.0;
    private Text tally;

    // A list-like container used to keep track of search results.
    private ObservableList<String> searchResults;
    
    public FSUserInterface()
    {
    }
    
    public void show(Stage stage)
    {
        stage.setTitle("File Searcher");
        
        FlowPane searchPanel = new FlowPane(SPACING, SPACING);
        TextField searchPathBox = new TextField("/usr");
        TextField searchTermBox = new TextField();
        Button searchButton = new Button("Search");
        
        searchPanel.getChildren().addAll(
            new Text("Path:"),
            searchPathBox,
            new Text("Search text:"),
            searchTermBox,
            searchButton);
            
        searchButton.setOnAction((event) ->
        {
            FSFileFinder finder = new FSFileFinder(
                searchPathBox.getText(),
                searchTermBox.getText(),
                this);
                
            finder.search();
        });
        
        ListView<String> resultsList = new ListView<>();
        searchResults = resultsList.getItems();
        
        FlowPane auxPanel = new FlowPane(SPACING, SPACING);
        auxPanel.setAlignment(Pos.BASELINE_RIGHT);
        tally = new Text();
        Button clearButton = new Button("Clear results");
        auxPanel.getChildren().addAll(tally, clearButton);
        
        clearButton.setOnAction((event) ->
        {
            searchResults.clear();
            tally.setText("");
        });
        
        BorderPane root = new BorderPane();
        BorderPane.setMargin(searchPanel, new Insets(SPACING));
        BorderPane.setMargin(resultsList, new Insets(0.0, SPACING, 0.0, SPACING));
        BorderPane.setMargin(auxPanel, new Insets(SPACING));
        root.setTop(searchPanel);
        root.setCenter(resultsList);
        root.setBottom(auxPanel);
        
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    
    public void addResult(String result)
    {
        searchResults.add(result);
        tally.setText(Integer.toString(searchResults.size()) + " result(s) found");
    }
    
    public void showError(String message)
    {
        Alert a = new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE);
        a.setResizable(true);
        a.showAndWait();
    }
}