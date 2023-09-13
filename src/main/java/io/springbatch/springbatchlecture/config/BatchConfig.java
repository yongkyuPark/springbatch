package io.springbatch.springbatchlecture.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing(executionContextSerializerRef = "jacksonSerializer")
@Configuration
public class BatchConfig {

    @Bean
    public ExecutionContextSerializer jacksonSerializer() {
        return new Jackson2ExecutionContextStringSerializer();
    }

    /**
     * JobRegistry는 context에서 Job을 추적할 때 유용합니다.
     * JobRegistryBeanPostProcessor는 Application Context가 올라가면서 bean 등록 시, 자동으로 JobRegistry에 Job을 등록 시켜줍니다.
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;

    }

}
