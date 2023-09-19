package io.springbatch.springbatchlecture.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;

import java.util.Map;
import java.util.Properties;

@Data
@Builder
@ToString
public class JobLauncherRequestVO {

    private String name;
   // private Properties jobParameters;




}
