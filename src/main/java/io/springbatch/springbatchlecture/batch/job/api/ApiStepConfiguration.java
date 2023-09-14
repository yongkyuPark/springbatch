package io.springbatch.springbatchlecture.batch.job.api;

import io.springbatch.springbatchlecture.batch.chunk.processor.ApiItemProcessor1;
import io.springbatch.springbatchlecture.batch.chunk.processor.ApiItemProcessor2;
import io.springbatch.springbatchlecture.batch.chunk.processor.ApiItemProcessor3;
import io.springbatch.springbatchlecture.batch.chunk.writer.ApiItemWriter1;
import io.springbatch.springbatchlecture.batch.chunk.writer.ApiItemWriter2;
import io.springbatch.springbatchlecture.batch.chunk.writer.ApiItemWriter3;
import io.springbatch.springbatchlecture.batch.classifier.ProcessorClassifier;
import io.springbatch.springbatchlecture.batch.classifier.WriterClassifier;
import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.batch.partition.ProductPartitioner;
import io.springbatch.springbatchlecture.service.ApiService1;
import io.springbatch.springbatchlecture.service.ApiService2;
import io.springbatch.springbatchlecture.service.ApiService3;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

    private final DataSource dataSource;
    private final ApiService1 apiService1;
    private final ApiService2 apiService2;
    private final ApiService3 apiService3;

    private int chunkSize = 10;

    @Bean
    public Step apiMasterStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                              Step apiSlaveStep) {
        return new StepBuilder("apiMasterStep", jobRepository)
                .partitioner(apiSlaveStep.getName(), partitioner()) // partitionStep 생성
                .step(apiSlaveStep) // slave 역할을 하는 Step 설정
                .gridSize(3)        // 몇개의 파티션으로 나눌 것인지
                .taskExecutor(taskExecutor()) // 스레드 생성
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);  // 기본적으로 3개의 스레드 생성
        taskExecutor.setMaxPoolSize(6);   // 스레드가 부족할 경우 최대 6개까지 스레드 생성
        taskExecutor.setThreadNamePrefix("api-thread-");

        return taskExecutor;
    }

    @Bean
    public Step apiSlaveStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("apiSlaveStep", jobRepository)
                .<ProductVO, ProductVO>chunk(chunkSize,platformTransactionManager)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ProductPartitioner partitioner() {
        ProductPartitioner productPartitioner = new ProductPartitioner();
        productPartitioner.setDataSource(dataSource);
        return productPartitioner;
    }

    @Bean
    @StepScope
    public ItemReader<ProductVO> itemReader(@Value("#{stepExecutionContext['product']}") ProductVO productVO) throws Exception {

        JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper(ProductVO.class));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("from product");
        queryProvider.setWhereClause("where type = :type");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.DESCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productVO.getType()));
        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet(); // reader 설정 후 초기화

        return reader;

    }

    @Bean
    public ItemProcessor itemProcessor() {

        // 입력받은 데이터를 기반으로 ItemProcessor를 구분하기 위한 로직
        ClassifierCompositeItemProcessor<ProductVO, ApiRequestVO> processor
                = new ClassifierCompositeItemProcessor<>();
        ProcessorClassifier<ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> classifier = new ProcessorClassifier();

        // 각 키값과 ApiItemProcessor 들을 저장 (각 스레드마다 다른 프로세서를 태우기 위함)
        Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();
        processorMap.put("1", new ApiItemProcessor1());
        processorMap.put("2", new ApiItemProcessor2());
        processorMap.put("3", new ApiItemProcessor3());

        classifier.setProcessorMap(processorMap);

        processor.setClassifier(classifier);

        return processor;
    }

    @Bean
    public ItemWriter itemWriter() {
        ClassifierCompositeItemWriter<ApiRequestVO> writer
                = new ClassifierCompositeItemWriter<>();
        WriterClassifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> classifier = new WriterClassifier();

        Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();
        writerMap.put("1", new ApiItemWriter1(apiService1));
        writerMap.put("2", new ApiItemWriter2(apiService2));
        writerMap.put("3", new ApiItemWriter3(apiService3));

        classifier.setProcessorMap(writerMap);

        writer.setClassifier(classifier);

        return writer;
    }
}
