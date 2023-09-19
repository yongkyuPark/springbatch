package io.springbatch.springbatchlecture.batch.job.api;

import io.springbatch.springbatchlecture.batch.listener.JobListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ApiJobChildConfiguration {

    private final Step apiMasterStep;
    private final JobLauncher jobLauncher;

    @Bean
    public Step jobStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, Job childJob) {
        return new StepBuilder("jobStep", jobRepository)
                .job(childJob)
                .launcher(jobLauncher)
                .build();
    }

    @Bean
    public Job childJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("childJob", jobRepository)
                .listener(new JobListener())
                .incrementer(new RunIdIncrementer())
                .start(apiMasterStep)
                .build();
    }
}
