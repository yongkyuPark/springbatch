package io.springbatch.springbatchlecture.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final JobOperator jobOperator;
    private final JobRegistry jobRegistry;
    private final JobExplorer jobExplorer;

    public void batchStop(String jobName) throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException {

        Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(jobName);
        JobExecution jobExecution = runningJobExecutions.iterator().next();

        jobOperator.stop(jobExecution.getId());
    }

    public void batchRestart(String jobName) throws JobInstanceAlreadyCompleteException, NoSuchJobException, NoSuchJobExecutionException, JobParametersInvalidException, JobRestartException {
        JobInstance lastJobInstance = jobExplorer.getLastJobInstance(jobName);
        JobExecution lastJobExecution = jobExplorer.getLastJobExecution(lastJobInstance);

        jobOperator.restart(lastJobExecution.getId());
    }

}
