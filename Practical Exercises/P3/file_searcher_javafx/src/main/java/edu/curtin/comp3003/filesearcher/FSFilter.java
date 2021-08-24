package edu.curtin.comp3003.filesearcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;

public class FSFilter {

    private String searchTerm = null;
    private FSUserInterface ui;
    private ExecutorService exec;

    public FSFilter(String searchTerm, FSUserInterface ui) {
        this.searchTerm = searchTerm;
        this.ui = ui;
        this.exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void addPath(String path) {
        // Send the executor a call to handlePath
        this.exec.submit(() -> handlePath(path));
    }

    public void shutdown() {
        // Tell the executor service that there's no more paths coming
        this.exec.shutdown();
    }

    // Search for the searchTerm in the file contents
    private void handlePath(String pathString) {
        Path path = Paths.get(pathString);
        boolean match = false;

        try (BufferedReader rdr = Files.newBufferedReader(path)) {
            String currLine = rdr.readLine();
            while (currLine != null && !match) {
                if (currLine.contains(this.searchTerm)) {
                    match = true;
                }
                currLine = rdr.readLine();
            }
        } catch (IOException e) {
            // Lets just ignore this file if its being problematic
        }

        if (match) {
            Platform.runLater(() -> this.ui.addResult(pathString));
        }
    }
}