package io.springbatch.springbatchlecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class HelloJobConfiguration {

//    @Bean
//    public DataSource batchDataSource() {
//        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//                .addScript("/org/springframework/batch/core/schema-h2.sql")
//                .generateUniqueName(true).build();
//    }
//
//    @Bean
//    public JdbcTransactionManager batchTransactionManager(DataSource dataSource) {
//        return new JdbcTransactionManager(dataSource);
//    }

    @Bean
    public Job helloJob(JobRepository jobRepository, Step helloStep1, Step helloStep2, Step customStep) {
        return new JobBuilder("helloJob", jobRepository)
                .start(helloStep1)
                .next(helloStep2)
                .next(customStep)
                .build();
    }

    @Bean
    public Step helloStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("helloStep1", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                        // 파라미터 가져오기
                        JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();

                        Map<String, Object> jobParameters1 = chunkContext.getStepContext().getJobParameters();

                        System.out.println(" =======================");
                        System.out.println(" >> Hello Spring Batch!!");
                        System.out.println(" =======================");

                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step helloStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("helloStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println(" =======================");
                    System.out.println(" >> step2 was executed");
                    System.out.println(" =======================");

                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step customStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("customStep", jobRepository)
                .tasklet(new CustomTasklet(), platformTransactionManager)
                .build();
    }
}
