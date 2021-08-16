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
            while (this.currentMessage != null) {
                this.monitor.wait();
            }
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
                    while (this.currentMessage == null) {
                        this.monitor.wait();
                    }
                    String message = this.currentMessage;
                    this.currentMessage = null;

                    try (PrintWriter pw = new PrintWriter(
                        new FileWriter("cron.log", true)
                    )) {
                        pw.println(message);
                    } catch (IOException e) {
                        System.out.println("An IO error occured");
                        e.printStackTrace();
                    }

                    this.monitor.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Logger closing");
        }
    }
}
