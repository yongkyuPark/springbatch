package io.springbatch.springbatchlecture.domain;

import lombok.Data;

@Data
public class JobSchedulerRequestVO {

    private String jobName;
    private String newCronExpression;

}
