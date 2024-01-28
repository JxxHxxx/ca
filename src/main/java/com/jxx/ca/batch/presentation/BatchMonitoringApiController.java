package com.jxx.ca.batch.presentation;

import com.jxx.ca.batch.presentation.dto.StepInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchMonitoringApiController {

    private final JobExplorer jobExplorer;

    @GetMapping("/api/job-executions/{job-execution-id}/step-executions/{step-execution-id}")
    public ResponseEntity<StepInfoResponse> moni(@PathVariable("job-execution-id") Long jobExecutionId, @PathVariable("step-execution-id") Long stepExecutionId) {
        StepExecution stepExecution = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);

        return ResponseEntity.ok(StepInfoResponse.from(stepExecution));
    }
}
