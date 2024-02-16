package com.jxx.ca.batch.generator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.lang.Nullable;

import java.util.Map;

@Slf4j
public class IdentifyJobParameterGenerator implements JobParametersIncrementer {

    private static String JOB_EXECUTION_KEY = "id";

    private String key = JOB_EXECUTION_KEY;

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public JobParameters getNext(@Nullable JobParameters parameters) { // 가장 마지막 파라미터가 들어오네
        if (parameters.isEmpty()) {
            Map<String, JobParameter> init = Map.of(key, new JobParameter("init", true));
            JobParameters jobParameters = new JobParameters(init);
            return new JobParametersBuilder(jobParameters).toJobParameters();
        }

        return new JobParametersBuilder(parameters)
                .toJobParameters();

    }
}
