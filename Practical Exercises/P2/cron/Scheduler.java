import java.util.*;

/**
 * The scheduler keeps track of all the jobs, and runs each one at the
 * appropriate time. (You need to fill in the details!)
 */
public class Scheduler
{
    private List<Job> jobList = Collections.synchronizedList(
        new ArrayList<Job>()
    );
    private Thread thread = new Thread(() -> {
        for (Job job : jobList) {
            
        }
    });

    public void addJob(Job newJob)
    {
        this.jobList.add(newJob);
    }
    
    public void start()
    {
        this.thread.start();
    }

    public void stop()
    {
        // ...
    }
}
