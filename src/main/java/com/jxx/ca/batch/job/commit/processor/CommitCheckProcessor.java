package com.jxx.ca.batch.job.commit.processor;

import com.jxx.ca.batch.job.commit.model.CommitCheckModel;
import com.jxx.ca.github.api.GithubRepoCommitHistoryApiAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CommitCheckProcessor implements ItemProcessor<CommitCheckModel, CommitCheckModel> {
    // 테스트 용이하게 하기 위해 인터페이스화
    private final GithubRepoCommitHistoryApiAdapter githubRepoCommitHistoryApiAdapter;

    @Override
    public CommitCheckModel process(CommitCheckModel item) throws Exception {
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        String sinceTime = (String) jobParameters.get("sinceTime");

        List commitHistory = githubRepoCommitHistoryApiAdapter.request(item.getGithubName(), item.getRecentlyPushedRepoName(), sinceTime);

        if (commitHistory.isEmpty()) {
            item.checkCommitDone(false);
        } else {
            item.checkCommitDone(true);
        }

        log.info("사용자:{} {}일 커밋 여부:{} ", item.getGithubName(), sinceTime, item.getDone());

        return item;
    }
}
