package io.springbatch.springbatchlecture.batch.job.move;

import io.springbatch.springbatchlecture.aTest.entity.Product;
import io.springbatch.springbatchlecture.bTest.entity.TestEntity;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MoveJobConfiguration {


    private final EntityManagerFactory mysqlEntityManagerFactory;

    @Autowired
    @Qualifier("postgresqlEntityManagerFactory")
    private EntityManagerFactory postgresqlEntityManagerFactory;

    @Bean
    public Job moveJob(JobRepository jobRepository, Step moveStep) {
        return new JobBuilder("moveJob", jobRepository)
                .start(moveStep)
                .build();
    }

    @Bean
    public Step moveStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("moveStep", jobRepository)
                .<TestEntity, Product>chunk(10, platformTransactionManager)
                .reader(moveReader())
                .processor(moveProcessor())
                .writer(moveWriter())
                .build();
    }

    @Bean
    public ItemReader<TestEntity> moveReader() {
        // 데이터 조회
        return new JpaPagingItemReaderBuilder<TestEntity>()
                .name("reader")
                .entityManagerFactory(mysqlEntityManagerFactory)
                .pageSize(10)
                .queryString("SELECT t FROM TestEntity t")
                .build();
    }

    @Bean
    public ItemProcessor<TestEntity, Product> moveProcessor() {
        return test -> Product.builder()
                .id(test.getMberSn().longValue())
                .type(test.getMberId())
                .price(test.getMberSn())
                .name(test.getMberNm())
                .build();
    }

    @Bean
    public JpaItemWriter<Product> moveWriter() {
        JpaItemWriter<Product> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(mysqlEntityManagerFactory);
        writer.setUsePersist(true);
        return writer;
    }

}
