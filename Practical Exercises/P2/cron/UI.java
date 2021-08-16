import java.util.*;

/**
 * A simple console-based UI. It does need to be run in its own thread, but (for
 * simplicity) this can be the same thread that calls it (rather than starting
 * one up explicitly).
 */
public class UI {
    private Scheduler scheduler;
    private Logger logger;

    public UI(Scheduler scheduler, Logger logger) {
        this.scheduler = scheduler;
        this.logger = logger;
    }

    /**
     * Runs the console-based menu system (in the current thread).
     */
    public void menu() {
        Scanner sc = new Scanner(System.in);
        char choice;
        do {
            System.out.print("(n) New command, or (x) Exit? ");
            choice = (sc.nextLine().toLowerCase() + " ").charAt(0);

            if (choice == 'n') {
                // Ask user for details of the new command

                String command;
                do {
                    System.out.print("Enter command: ");
                    command = sc.nextLine();
                } while (command == null || command.length() == 0);

                int delay = 0;
                do {
                    try {
                        System.out.print("How many seconds between executions? ");
                        delay = sc.nextInt();
                        sc.nextLine();
                    } catch (InputMismatchException e) {
                        // Non-numeric input; continue loop.
                    }
                } while (delay <= 0);

                // Add new job to the scheduler. (NOTE: you'll need to change
                // the code here.)
                Job job = new Job(command, delay, logger);
                this.scheduler.addJob(job);
            }
        } while (choice != 'x');

        // When the user wants to exit, we need to shut down the other threads.
        scheduler.stop();
        logger.stop();
    }
}
