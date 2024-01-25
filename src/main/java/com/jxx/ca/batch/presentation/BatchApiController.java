package com.jxx.ca.batch.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BatchApiController {
    private final ApplicationContext context; // Job Bean 찾기
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;

    @PostMapping("/batch/run")
    public ResponseEntity runJob(@RequestBody JobLauncherRequest request) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Job job = context.getBean(request.getJobName(), Job.class);

        JobParameters jobParameters = new JobParametersBuilder(request.getJobParameters(), jobExplorer)
                .getNextJobParameters(job)
                .toJobParameters();

        ExitStatus exitStatus = jobLauncher.run(job, jobParameters)
                .getExitStatus();
        return ResponseEntity.ok(exitStatus.toString());
    }
}
