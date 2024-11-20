package com.example.demo.task;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Scheduled(cron = "0 * * * * ?")
    @SchedulerLock(name = "uniqueTask", lockAtLeastFor = "PT10S")
    public void uniqueTask()
    {
        System.out.println("Executing unique task at " + System.currentTimeMillis());
    }
}
