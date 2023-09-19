package io.springbatch.springbatchlecture.controller;

import io.springbatch.springbatchlecture.domain.JobLauncherRequestVO;
import io.springbatch.springbatchlecture.domain.ProductVO;
import io.springbatch.springbatchlecture.study.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobLauncherController {

    private final JobLauncher jobLauncher;

    private final Job fileJob;

    private final Job apiJob;

    private final Job moveJob;

    private final JobExplorer jobExplorer;

//    @PostMapping("/launcher")
//    public ExitStatus launchJob(@RequestBody JobLauncherRequestVO request) throws Exception {
//        Job job = jobRegistry.getJob(request.getName());
//        return this.jobLauncher.run(job, request.getJobParameters()).getExitStatus();
//    }

    /**
     * 파일을 읽은 후 Product 테이블에 저장하는 잡
     * @param productVO
     * @return
     * @throws JobInstanceAlreadyCompleteException
     * @throws JobExecutionAlreadyRunningException
     * @throws JobParametersInvalidException
     * @throws JobRestartException
     * @throws NoSuchJobException
     */
    @PostMapping("/fileJobLauncher")
    public ExitStatus fileJobLauncher(@RequestBody ProductVO productVO) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, NoSuchJobException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", productVO.getRequestDate())
                .toJobParameters();

        long jobInstanceCount = jobExplorer.getJobInstanceCount(fileJob.getName());
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(fileJob.getName(), 0, (int) jobInstanceCount);

        if(jobInstances.size() > 0) {
            for(JobInstance jobInstance : jobInstances) {
                List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
                List<JobExecution> jobExecutionList = jobExecutions.stream().filter(jobExecution ->
                        jobExecution.getJobParameters().getString("requestDate").equals(productVO.getRequestDate())).collect(Collectors.toList());
                if(jobExecutionList.size() > 0) {
                    throw new JobInstanceAlreadyCompleteException(productVO.getRequestDate() + " already exists");
                }
            }
        }

        JobExecution run = jobLauncher.run(fileJob, jobParameters);

        return run.getExitStatus();
    }

    /**
     * Product 테이블 데이터를 읽어서 api 서버와 통신하는 잡
     * @return
     * @throws JobInstanceAlreadyCompleteException
     * @throws JobExecutionAlreadyRunningException
     * @throws JobParametersInvalidException
     * @throws JobRestartException
     */
    @PostMapping("/apiJobLauncher")
    public ExitStatus apiJobLauncher() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .toJobParameters();

        JobExecution run = jobLauncher.run(apiJob, jobParameters);

        return run.getExitStatus();
    }

    @PostMapping("/moveJobLauncher")
    public ExitStatus moveJobLauncher() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .toJobParameters();

        JobExecution run = jobLauncher.run(moveJob, jobParameters);

        return run.getExitStatus();

    }

}
