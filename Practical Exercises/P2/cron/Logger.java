import java.io.*;

/**
 * The logger is in charge of writing output to 'cron.log'. It does this in its
 * own thread, but assumes that other threads will call the setMessage() in
 * order to provide messages to log. (You need to fill in the details!)
 */
public class Logger {
    private final Thread thread = new Thread(this::run);
    private final Object monitor = new Object();
    private String currentMessage = null;

    public void setMessage(String newMessage) throws InterruptedException {
        synchronized (this.monitor) {
            // Wait until the logger thread has cleared the message
            while (this.currentMessage != null) {
                this.monitor.wait();
            }
            // Set the message field and notify the monitor
            this.currentMessage = newMessage;
            this.monitor.notifyAll();
        }
    }

    public void start() {
        this.thread.start();
    }

    public void stop() {
        this.thread.interrupt();
    }

    private void run() {
        try {
            synchronized (monitor) {
                while (true) {
                    // Wait until another thread sets the message
                    while (this.currentMessage == null) {
                        this.monitor.wait();
                    }
                    // Grab the non-null current message and clear the field
                    String localMessage = this.currentMessage;
                    this.currentMessage = null;
                    // Notify the other threads
                    this.monitor.notifyAll();

                    // Append to the logging file
                    try (PrintWriter pw = new PrintWriter(
                        new FileWriter("cron.log", true)
                    )) {
                        pw.println(localMessage);
                    } catch (IOException e) {
                        System.out.println("An IO error occured");
                        e.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException e) {
            // Expected - Do nothing
        }
    }
}
