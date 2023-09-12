package io.springbatch.springbatchlecture.study;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ItemStreamConfiguration {

    @Bean
    public Job ItemStreamJob(JobRepository jobRepository, Step step1, Step step2) {
        return new JobBuilder("itemStreamJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<String, String>chunk(5, platformTransactionManager)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    public ItemWriter<? super String> itemWriter() {

        return new CustomItemWriter();
    }

    public CustomItemStreamReader itemReader() {
        List<String> items = new ArrayList<>(10);
        for(int i = 0; i <= 10; i++) {
            items.add(String.valueOf(i));
        }
        return new CustomItemStreamReader(items);
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println(" =======================");
                    System.out.println(" >> step5 was executed");
                    System.out.println(" =======================");

                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }


}
