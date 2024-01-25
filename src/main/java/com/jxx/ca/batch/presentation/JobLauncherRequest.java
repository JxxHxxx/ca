package com.jxx.ca.batch.presentation;

import lombok.Getter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Properties;


@Getter
public class JobLauncherRequest {
    private String jobName;
    private Properties jobParameters;

    public JobParameters getJobParameters() {
        Properties properties = new Properties();

        if (jobParameters == null) {
            return new JobParametersBuilder(properties)
                    .toJobParameters();
        }

        properties.putAll(this.jobParameters);

        return new JobParametersBuilder(properties)
                .toJobParameters();
    }
}
