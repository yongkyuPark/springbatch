package io.springbatch.springbatchlecture.service;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final Scheduler scheduler;

    public void changeScheduleExpression(String jobName, String newCronExpression) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName);
        CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        if(oldTrigger != null) {
            CronTrigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(newCronExpression))
                    .build();

            scheduler.rescheduleJob(triggerKey, newTrigger);
        }
    }

}
