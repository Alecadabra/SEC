package edu.curtin.comp3003.filesearcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import javafx.application.Platform;

public class FSFilter {

    private BlockingQueue<String> queue = null;
    private String searchTerm = null;
    private Thread thread = null;
    private FSUserInterface ui;

    public FSFilter(String searchTerm, FSUserInterface ui) {
        this.searchTerm = searchTerm;
        this.ui = ui;
    }

    public void start() {
        this.queue = new SynchronousQueue<>();

        if (this.thread != null) {
            this.thread.interrupt();
        }
        this.thread = new Thread(this::run);
        this.thread.start();
    }

    public void addPath(String path) throws InterruptedException {
        this.queue.put(path);
    }

    private void run() {
        try {
            while (true) {
                // Await the next path
                String currentPath = queue.take();

                if (currentPath.contains(searchTerm)) {
                    // Add to the results
                    Platform.runLater(() -> ui.addResult(currentPath));
                }
            }
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

}