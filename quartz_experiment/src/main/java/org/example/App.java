package org.example;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws SchedulerException {
        Map<String, String> quartzProps =
                Map.of(

                );

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        scheduler.getListenerManager().addTriggerListener(new JobCompletionListener());

        for(int i = 0; i < 10000; ++i) {
            JobDetail job = JobBuilder.newJob(ExampleJob.class)
                    .withIdentity("myTrigger" + i, "group" + i)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTrigger" + i, "group" + i)
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(1)
                            .repeatForever())
                    .build();

            scheduler.scheduleJob(job, trigger);
        }

        scheduler.start();
        while (true);
    }
}
