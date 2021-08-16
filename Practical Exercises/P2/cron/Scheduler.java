import java.util.*;

/**
 * The scheduler keeps track of all the jobs, and runs each one at the
 * appropriate time. (You need to fill in the details!)
 */
public class Scheduler {
    private final List<Job> jobList
        = Collections.synchronizedList(new ArrayList<Job>());
    private final Thread thread = new Thread(this::run);
    private int secondsPassed = 0;

    public void addJob(Job newJob) {
        this.jobList.add(newJob);
    }

    public void start() {
        this.thread.start();
    }

    public void stop() {
        this.thread.interrupt();
    }

    // The scheduler logic that runs in the thread.
    private void run() {
        // Initialise variables
        this.secondsPassed = 0;
        long startTimeMs = 0;
        long sleepOffsetMs = 0;

        // Run until interrupted
        try {
            // Loop forever
            while (true) {
                // Record time before starting jobs
                startTimeMs = System.currentTimeMillis();
                // Run through each job
                for (Job job : jobList) {
                    // Start job if current second aligns with delay
                    if (this.secondsPassed % job.getDelay() == 0) {
                        new Thread(job).start();
                    }
                }
                // Get the sleep offset
                // If the for loop took 5ms, this would be 5
                sleepOffsetMs = System.currentTimeMillis() - startTimeMs;
                // Sleep for the remainder of the second
                Thread.sleep(Math.max(0, 1_000 - sleepOffsetMs));
                // Increment the seconds counter
                this.secondsPassed++;
            }
        } catch (InterruptedException e) {
            System.out.println(
                "Cron stopped due to interruption (" + e.getMessage() + ")"
            );
        }
    }
}
