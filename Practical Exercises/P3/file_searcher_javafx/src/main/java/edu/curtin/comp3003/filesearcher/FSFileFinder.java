package edu.curtin.comp3003.filesearcher;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import javafx.application.Platform;

public class FSFileFinder {
    private String searchPath;
    private FSUserInterface ui;
    private FSFilter filter;
    private Thread thread;

    public FSFileFinder(String searchPath, String searchTerm, FSUserInterface ui) {
        this.searchPath = searchPath;
        this.ui = ui;
        this.filter = new FSFilter(searchTerm, ui);
        this.thread = new Thread(this::run);
    }

    public void search() {
        this.thread.start();
    }

    private void run() {
        this.filter.start();
        try {
            // Recurse through the directory tree
            Files.walkFileTree(Paths.get(searchPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        // Add the new path to the filter's queue
                        FSFileFinder.this.filter.addPath(file.toString());
                    } catch (InterruptedException e) {
                        // If interrupted, stop the search
                        return FileVisitResult.TERMINATE;
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // This error handling is a bit quick-and-dirty, but it will suffice here.
            Platform.runLater(() -> {
                ui.showError(e.getClass().getName() + ": " + e.getMessage());
            });
        }
    }
}
