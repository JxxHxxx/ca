package com.jxx.ca.batch.job.commit.processor;

import com.jxx.ca.batch.job.commit.model.CommitCheckModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;

import java.util.Map;

@Slf4j
public class ActiveValidateProcessor implements ItemProcessor<CommitCheckModel, CommitCheckModel>{

    @Override
    public CommitCheckModel process(CommitCheckModel item) throws Exception {
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        String sinceTime = (String) jobParameters.get("sinceTime");

        boolean isActive = item.isActiveMember();

        log.info("사용자:{} {}일 활성화 여부 :{} ", item.getGithubName(), sinceTime, isActive);

        return isActive ? item : null;
    }
}
