package io.springbatch.springbatchlecture.batch.job.move;

import io.springbatch.springbatchlecture.aTest.entity.Product;
import io.springbatch.springbatchlecture.aTest.repository.ProductDetailRepository;
import io.springbatch.springbatchlecture.aTest.repository.ProductRepository;
import io.springbatch.springbatchlecture.bTest.entity.TestEntity;
import io.springbatch.springbatchlecture.bTest.repository.BTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MoveJobConfiguration3 {

    private final ProductRepository productRepository; // 1번 디비 상품 테이블
    private final ProductDetailRepository productDetailRepository; // 1번 디비 상품 디테일 테이블
    private final BTestRepository bTestRepository; // 2번 디비 회원 테이블

    @Bean
    public Job moveJob3(JobRepository jobRepository, Step moveStep3_1, Step moveStep3_2) {
        return new JobBuilder("moveJob3", jobRepository)
                .start(moveStep3_1)
                .next(moveStep3_2)
                .build();
    }

    @Bean
    public Step moveStep3_1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("moveStep3_1", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("========= step1 was executed ===========");
                        ExecutionContext executionContext = contribution.getStepExecution()
                                .getJobExecution()
                                .getExecutionContext();

                        // 데이터 조회
                        List<TestEntity> memberList = bTestRepository.findAll();

                        executionContext.put("memberList", memberList);

                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step moveStep3_2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("moveStep3_2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        // 데이터 저장
                        System.out.println("========= step2 was executed ===========");

                        ExecutionContext executionContext = contribution.getStepExecution()
                                .getJobExecution().getExecutionContext();

                        List<TestEntity> memberList = (List<TestEntity>) executionContext.get("memberList");
                        List<Product> productList = new ArrayList<>();

                        memberList.forEach(getTestEntity -> {
                            Product product = getTestEntity.toProduct();
                            productList.add(product);
                        });

                        //throw new IllegalStateException("test");

                        productRepository.saveAll(productList);

                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }

}
