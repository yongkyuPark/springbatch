package io.springbatch.springbatchlecture.scheduler;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@Component
public class FileJobRunner extends JobRunner {

    @Autowired
    private Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {

        String[] sourceArgs = args.getSourceArgs();

        JobDetail jobDetail = buildJobDetail(FileSchJob.class, "fileJob", "batch", new HashMap<>());
        Trigger trigger = buildJobTrigger("0/50 * * * * ?");
        // todo argument에서 임시로 세팅한 데이터
        jobDetail.getJobDataMap().put("requestDate", sourceArgs[0]);

        try{
            scheduler.scheduleJob(jobDetail, trigger);
        }catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
