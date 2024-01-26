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

        Object idProperty = properties.get("id");
        if (idProperty == null) {
            throw new IllegalArgumentException("JobParameter id는 잡 식별을 위한 필수 값입니다.");
        }
        String id = idProperty.toString();

        return new JobParametersBuilder(properties).addString("id", id, true)
                .toJobParameters();
    }
}
