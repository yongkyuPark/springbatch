package io.springbatch.springbatchlecture.scheduler;

import io.springbatch.springbatchlecture.aTest.entity.Batch;
import io.springbatch.springbatchlecture.aTest.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.quartz.JobBuilder.newJob;

@Component
@RequiredArgsConstructor
public class ApiJobRunner extends JobRunner {

    @Autowired
    private Scheduler scheduler;

    private final BatchRepository batchRepository;

    @Override
    protected void doRun(ApplicationArguments args) {

        Batch apiJob = batchRepository.findByJobName("apiJob")
                .orElseThrow(() -> new IllegalArgumentException("실행할 배치가 없습니다."));

        JobDetail jobDetail = buildJobDetail(ApiSchJob.class, apiJob.getJobName(), "batch", new HashMap<>()); // class명도 디비에 저장
        Trigger trigger = buildJobTrigger(apiJob.getCron(), apiJob.getJobName());

        try{
            scheduler.scheduleJob(jobDetail, trigger); // 트리거 갱신 가능?
        }catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
