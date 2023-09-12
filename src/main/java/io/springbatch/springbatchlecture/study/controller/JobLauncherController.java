package io.springbatch.springbatchlecture.study.controller;

import io.springbatch.springbatchlecture.study.dto.MemberDto;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class JobLauncherController {

//    @Autowired
//    private Job job;

    @Autowired
    private JobLauncher jobLauncher;


    /**
     * 동기식 방식
     * @param memberDto
     * @return
     * @throws JobInstanceAlreadyCompleteException
     * @throws JobExecutionAlreadyRunningException
     * @throws JobParametersInvalidException
     * @throws JobRestartException
     */
    @PostMapping("/batch")
    public String launch(@RequestBody MemberDto memberDto) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", memberDto.getId())
                .addDate("date", new Date())
                .toJobParameters();

        //jobLauncher.run(job, jobParameters);

        return "batch completed";
    }

}
