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

        String id = properties.get("id").toString();

        if (id.isEmpty() || id.isBlank()) {
            throw new IllegalArgumentException("id 잡을 식별하는 필수 값입니다.");
        }

        return new JobParametersBuilder(properties).addString("id", id, true)
                .toJobParameters();
    }
}
