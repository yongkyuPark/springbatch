package io.springbatch.springbatchlecture.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.Duration;

public class JobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        Duration duration = Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime());

        long seconds = duration.getSeconds() % 60;

        System.out.println("총 소요시간 = " + seconds);
    }

}
