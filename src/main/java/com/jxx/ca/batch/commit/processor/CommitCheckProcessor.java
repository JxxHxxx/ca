package com.jxx.ca.batch.commit.processor;

import com.jxx.ca.batch.commit.model.CommitCheckModel;
import com.jxx.ca.github.api.CommitHistoryApiAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CommitCheckProcessor implements ItemProcessor<CommitCheckModel, CommitCheckModel> {
    // 테스트 용이하게 하기 위해 인터페이스화
    private final CommitHistoryApiAdapter commitHistoryApiAdapter;

    @Override
    public CommitCheckModel process(CommitCheckModel item) throws Exception {
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        String sinceTime = (String) jobParameters.get("sinceTime");

        List commitHistory = commitHistoryApiAdapter.getResponseBody(item.getGithubName(), item.getRecentlyPushedRepoName(), sinceTime);

        if (commitHistory.isEmpty()) {
            item.checkCommitDone(false);
        } else {
            item.checkCommitDone(true);
        }

        log.info("사용자:{} {}일 커밋 여부:{} ", item.getGithubName(), sinceTime, item.getDone());

        return item;
    }
}
