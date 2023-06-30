package dialog.scheduler;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedules;

import java.time.Duration;

public class JobScheduler {
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        scheduler.schedule(
                () -> System.out.println("My first job"),           // the runnable to be scheduled
                Schedules.fixedDelaySchedule(Duration.ofMinutes(1)) // the schedule associated with the runnable
        );
    }
}
