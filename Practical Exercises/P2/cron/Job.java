import java.io.*;

/**
 * Represents a job (command) to be run, and performs the execution. (You need
 * to fill in the details!)
 */
public class Job implements Runnable {
    private String command;
    private int delay;
    private Logger logger;

    public Job(String command, int delay, Logger logger) {
        this.command = command;
        this.delay = delay;
        this.logger = logger;
    }

    public int getDelay() {
        return this.delay;
    }

    @Override
    public void run() {
        try {
            // Assume 'command' is a string containing the command the
            // execute. Then we initially run it as follows:
            Process proc = Runtime.getRuntime().exec(command);
            // Arrange to capture the command's output, line by line.
            StringBuilder output = new StringBuilder();
            InputStream inputStream = proc.getInputStream();
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                output.append('\n');
                line = reader.readLine();
            }

            // We've now reached the end of the command's output, which
            // generally means the command has finished.
            StringBuilder sb = new StringBuilder();
            sb.append("-------------------\n");
            sb.append("New output\n");
            sb.append("- Command: '" + this.command + "'\n");
            sb.append("- Delay:   " + this.delay + "\n");
            sb.append("- Output string:\n");
            sb.append(output.toString());
            sb.append("-------------------\n\n");

            this.logger.setMessage(sb.toString());
            
        } catch (IOException e) {
            System.out.println(
                "Job \"" + this.command + "\" encountered an IO error"
            );
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println(
                "Job \"" + this.command
                + "\" was interrupted while updating the log file"
            );
        }
    }
}
