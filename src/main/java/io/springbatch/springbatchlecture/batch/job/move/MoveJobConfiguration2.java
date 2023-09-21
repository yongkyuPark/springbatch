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
public class MoveJobConfiguration2 {

    private final EntityManagerFactory mysqlEntityManagerFactory;

    @Autowired
    @Qualifier("postgresqlEntityManagerFactory")
    private EntityManagerFactory postgresqlEntityManagerFactory;

    @Autowired
    @Qualifier("postgresqlTransactionManager")
    private PlatformTransactionManager postgresqlTransactionManager;

    @Bean
    public Job moveJobTest(JobRepository jobRepository, Step moveStep2) {
        return new JobBuilder("moveJobTest", jobRepository)
                .start(moveStep2)
                .build();
    }

    @Bean
    public Step moveStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("moveStep2", jobRepository)
                .<Product, TestEntity>chunk(10, postgresqlTransactionManager)
                .reader(moveReader2())
                .processor(moveProcessor2())
                .writer(moveWriter2())
                .build();
    }

    @Bean
    public ItemReader<Product> moveReader2() {
        // 데이터 조회
        return new JpaPagingItemReaderBuilder<Product>()
                .name("reader2")
                .entityManagerFactory(mysqlEntityManagerFactory)
                .pageSize(10)
                .queryString("SELECT p FROM Product p")
                .build();
    }

    @Bean
    public ItemProcessor<Product, TestEntity> moveProcessor2() {
        return test -> TestEntity.builder()
                .mberSn(test.getPrice())
                .mberId(test.getName())
                .adres(test.getType())
                .build();
    }

    @Bean
    public JpaItemWriter<TestEntity> moveWriter2() {
        JpaItemWriter<TestEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(postgresqlEntityManagerFactory);
        writer.setUsePersist(false);
        return writer;
    }

}