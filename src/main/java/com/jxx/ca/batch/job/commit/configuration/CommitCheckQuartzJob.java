package com.jxx.ca.batch.job.commit.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CommitCheckQuartzJob extends QuartzJobBean {

    @Qualifier(value = "commit-check.job")
    private final Job job;
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;

    private static final String SINCE_TIME_SUFFIX_VALUE = "T00:00:00Z";

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("execute commit-check.job");
        String sinceTime = LocalDate.now() + SINCE_TIME_SUFFIX_VALUE;

        JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
                .addString("id", UUID.randomUUID().toString(), true)
                .addString("executeSystem", "Quartz")
                .addString("sinceTime", sinceTime)
                .toJobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}