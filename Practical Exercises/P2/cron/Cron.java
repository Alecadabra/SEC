/**
 * Entry point for the Cron program, for running jobs periodically.
 */
public class Cron
{
    public static void main(String[] args)
    {
        // Start the logger, start the scheduler, start the UI, and leave them
        // to it!
        
        Logger logger = new Logger();
        logger.start();
        
        Scheduler scheduler = new Scheduler();
        scheduler.start();        
        
        new UI(scheduler, logger).menu();
    }
}
