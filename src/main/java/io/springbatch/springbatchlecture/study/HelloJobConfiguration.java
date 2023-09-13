//package io.springbatch.springbatchlecture.study;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.*;
//import org.springframework.batch.core.job.builder.FlowBuilder;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.job.flow.Flow;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.item.Chunk;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.support.ListItemReader;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import java.util.Arrays;
//import java.util.Map;
//
//@Configuration
//@RequiredArgsConstructor
//public class HelloJobConfiguration {
//
//    private final JobExecutionListener jobExecutionListener;
//
////    @Bean
////    public DataSource batchDataSource() {
////        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
////                .addScript("/org/springframework/batch/core/schema-h2.sql")
////                .generateUniqueName(true).build();
////    }
////
////    @Bean
////    public JdbcTransactionManager batchTransactionManager(DataSource dataSource) {
////        return new JdbcTransactionManager(dataSource);
////    }
//
//    @Bean
//    public Job BatchJob(JobRepository jobRepository, Step helloStep1, Step helloStep2, Step customStep, Step chunkStep) {
//        return new JobBuilder("batchJob", jobRepository)
////                .incrementer(new CustomJobParametersIncrementer())
//                .incrementer(new RunIdIncrementer())
//                .start(jobStepTest(jobRepository,null, helloStep1))
//                .next(helloStep2)
////                .next(customStep)
////                .validator(new CustomJobParametersValidator())
////                .validator(new DefaultJobParametersValidator(new String[]{"name","date"}, new String[]{"count"}))
////                .preventRestart() // 재시작을 하지 않음
//                .listener(jobExecutionListener)
//                .build();
//    }
//
////    @Bean
////    public Job BatchJob2(JobRepository jobRepository, Flow flow, Step step5) {
////        return new JobBuilder("batchJob2", jobRepository)
////                .start(flow)
////                .next(step5)
////                .build().build();
////    }
//
//    @Bean
//    public Job childJobTest(JobRepository jobRepository, Step helloStep1) {
//        return new JobBuilder("childJob", jobRepository)
//                .start(helloStep1)
//                .build();
//    }
//
//    @Bean
//    public Step jobStepTest(JobRepository jobRepository, JobLauncher jobLauncher, Step helloStep1) {
//        return new StepBuilder("jobStep", jobRepository)
//                .job(childJobTest(jobRepository, helloStep1))
//                .launcher(jobLauncher)
//                .parametersExtractor(jobParametersExtractor())
//                .listener(new StepExecutionListener() {
//                    @Override
//                    public void beforeStep(StepExecution stepExecution) {
//                        stepExecution.getExecutionContext().putString("name", "user1");
//                    }
//
//                    @Override
//                    public ExitStatus afterStep(StepExecution stepExecution) {
//                        return null;
//                    }
//                })
//                .build();
//    }
//
//    private DefaultJobParametersExtractor jobParametersExtractor() {
//        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
//        extractor.setKeys(new String[]{"name"});
//        return extractor;
//    }
//
//    @Bean
//    public Step helloStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("helloStep1", jobRepository)
//                .tasklet(new Tasklet() {
//                    @Override
//                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                        // 파라미터 가져오기
//                        JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
//
//                        Map<String, Object> jobParameters1 = chunkContext.getStepContext().getJobParameters();
//
//                        System.out.println(" =======================");
//                        System.out.println(" >> Hello Spring Batch!!");
//                        System.out.println(" =======================");
//
//                        return RepeatStatus.FINISHED;
//                    }
//                }, platformTransactionManager)
////                .allowStartIfComplete(true)
//                .build();
//    }
//
//    @Bean
//    public Step helloStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("helloStep2", jobRepository)
//                .tasklet((contribution, chunkContext) -> {
//                    //throw new RuntimeException("step2 failed");
//                    System.out.println(" =======================");
//                    System.out.println(" >> step2 was executed");
//                    System.out.println(" =======================");
//
//                    return RepeatStatus.FINISHED;
//                }, platformTransactionManager)
////                .startLimit(3)
//                .build();
//    }
//
//    @Bean
//    public Step customStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("customStep", jobRepository)
//                .tasklet(new CustomTasklet(), platformTransactionManager)
//                .build();
//    }
//
//    @Bean
//    public Flow flow(Step step3, Step step4) {
//        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
//        flowBuilder.start(step3)
//                .next(step4)
//                .end();
//
//        return flowBuilder.build();
//    }
//
//    @Bean
//    public Step step3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("step3", jobRepository)
//                .tasklet((contribution, chunkContext) -> {
//
//                    System.out.println(" =======================");
//                    System.out.println(" >> step3 was executed");
//                    System.out.println(" =======================");
//
//                    return RepeatStatus.FINISHED;
//                }, platformTransactionManager)
//                .build();
//    }
//
//    @Bean
//    public Step step4(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("step4", jobRepository)
//                .tasklet((contribution, chunkContext) -> {
//
//                    System.out.println(" =======================");
//                    System.out.println(" >> step4 was executed");
//                    System.out.println(" =======================");
//
//                    return RepeatStatus.FINISHED;
//                }, platformTransactionManager)
//                .build();
//    }
//
//    @Bean
//    public Step step5(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("step5", jobRepository)
//                .tasklet((contribution, chunkContext) -> {
//
//                    System.out.println(" =======================");
//                    System.out.println(" >> step5 was executed");
//                    System.out.println(" =======================");
//
//                    return RepeatStatus.FINISHED;
//                }, platformTransactionManager)
//                .build();
//    }
//
//    @Bean
//    public Step chunkStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("chunkStep", jobRepository)
//                .<String, String>chunk(10, platformTransactionManager)
//                .reader(new ListItemReader<>(Arrays.asList("item1","item2","item3","item4","item5")))
//                .processor(new ItemProcessor<String, String>() {
//                    @Override
//                    public String process(String item) throws Exception {
//                        return item.toUpperCase();
//                    }
//                })
//                .writer(new ItemWriter<String>() {
//                    @Override
//                    public void write(Chunk<? extends String> items) throws Exception {
//                        items.forEach(item -> System.out.println(item));
//                    }
//                })
//                .build();
//    }
//
//}
