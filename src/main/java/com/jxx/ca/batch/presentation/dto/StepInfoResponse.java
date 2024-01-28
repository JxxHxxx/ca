package com.jxx.ca.batch.presentation.dto;


import org.springframework.batch.core.StepExecution;

public record StepInfoResponse(
        String stepName,
        String exitCode,
        String exitMessage,
        int commitCount,
        int readCount,
        int filterCount,
        int writeCount,
        int readSkipCount,
        int processSkipCount,
        int writeSkipCount,
        int rollbackCount
) {
        public static StepInfoResponse from(StepExecution stepExecution) {
            return new StepInfoResponse(
                    stepExecution.getStepName(),
                    stepExecution.getExitStatus().getExitCode(),
                    stepExecution.getExitStatus().getExitDescription(),
                    stepExecution.getCommitCount(),
                    stepExecution.getReadCount(),
                    stepExecution.getFilterCount(),
                    stepExecution.getWriteCount(),
                    stepExecution.getReadSkipCount(),
                    stepExecution.getProcessSkipCount(),
                    stepExecution.getWriteSkipCount(),
                    stepExecution.getRollbackCount()
                    );
        }
}
