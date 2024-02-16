package com.jxx.ca.batch.job.commit.configuration;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CommitCheckQuartzConfiguration {

    @Value("${batch.exec-time.cron.job.commit-check}")
    private String executionTime;

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(CommitCheckQuartzJob.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger() {
        log.info("executionTime {}", executionTime);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(executionTime);

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withSchedule(cronScheduleBuilder)
                .build();
    }
}
