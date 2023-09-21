package io.springbatch.springbatchlecture.domain;

import lombok.*;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ToString
public class JobLauncherRequestVO {

    private String name;
    private Map<String, Object> jobParameters;

    public JobParameters getJobParameters() {

        Map<String, JobParameter<?>> paramsMap = new HashMap<>();

        if(jobParameters != null) {
            for(Map.Entry<String, Object> entry : jobParameters.entrySet()) {
                String paramName = entry.getKey();
                Object paramValue = entry.getValue();

                JobParameter jobParam = new JobParameter(paramValue, paramValue.getClass());
                paramsMap.put(paramName, jobParam);
            }
        }else {
            JobParameter jobParam = new JobParameter(new Date().getTime(), Long.class);
            paramsMap.put("id", jobParam);
        }

        JobParameters realJobParams = new JobParameters(paramsMap);
        System.out.println("test ==========>  " + realJobParams);

        return new JobParametersBuilder(realJobParams).toJobParameters();
    }


}
