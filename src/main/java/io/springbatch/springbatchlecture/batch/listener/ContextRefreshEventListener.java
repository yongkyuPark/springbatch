package io.springbatch.springbatchlecture.batch.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ContextRefreshEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private final JobExplorer jobExplorer;
    private final JobRepository jobRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // was를 강제적으로 종료 할 경우 execution에 상태값이 started로 남아있게 되기 때문에 failed 처리
        // 배치 서버가 2대 이상일 경우 유의해야한다.
        // (배치 1번 서버를 재기동한 후 배치 2번 서버를 재기동 하기 전, 그 찰나의 순간에 스케쥴링 되어 있는 배치가 배치 1번 서버에서 돌게 되면,
        // 배치 2번 서버 재기동 시 배치 1번 서버에서 실행된 배치 Job의 상태를 FAILED로 바꿀 수 있기 때문)
        // 재기동 시에는 스케줄을 중지해 놓고, 모든 서버 재기동 후 스케줄을 원상복구 하거나
        // 상태 변경 로직을 수동으로 호출하는 로직 개발 필요
        List<String> jobs = jobExplorer.getJobNames();
        for (String job : jobs) {
            Set<JobExecution> runningJobs = jobExplorer.findRunningJobExecutions(job);

            for(JobExecution runningJob : runningJobs) {
                try {
                    if(!runningJob.getStepExecutions().isEmpty()) {
                        Iterator<StepExecution> iter = runningJob.getStepExecutions().iterator();
                        while (iter.hasNext()) {
                            StepExecution runningStep = (StepExecution) iter.next();
                            if(runningStep.getStatus().isRunning()) {
                                runningStep.setEndTime(LocalDateTime.now());
                                runningStep.setStatus(BatchStatus.FAILED);
                                runningStep.setExitStatus(new ExitStatus("FAILED", "BATCH FAILED"));
                                jobRepository.update(runningStep);

                            }
                        }
                    }
                    runningJob.setEndTime(LocalDateTime.now());
                    runningJob.setStatus(BatchStatus.FAILED);
                    runningJob.setExitStatus(new ExitStatus("FAILED", "BATCH FAILED"));
                    jobRepository.update(runningJob);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
