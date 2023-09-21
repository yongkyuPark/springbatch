package io.springbatch.springbatchlecture.batch.job.test;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class TestJobConfiguration {

    @Bean
    public Job testJob(JobRepository jobRepository, Step testStep) {
        return new JobBuilder("testJob", jobRepository)
                .start(testStep)
                .build();
    }

    @Bean
    public Step testStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("testStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                        JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();

                        System.out.println(" =======================");
                        System.out.println(" >> Hello Spring Batch!!");
                        System.out.println(" =======================");

                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }

}
