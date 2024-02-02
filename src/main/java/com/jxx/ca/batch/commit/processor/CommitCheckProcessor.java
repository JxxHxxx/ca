package com.jxx.ca.batch.commit.processor;

import com.jxx.ca.batch.commit.reader.CommitCheckModel;
import com.jxx.ca.github.api.CommitHistoryApiAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CommitCheckProcessor implements ItemProcessor<CommitCheckModel, CommitCheckModel> {

    private final CommitHistoryApiAdapter<CommitCheckModel> commitHistoryApiAdapter;

    @Override
    public CommitCheckModel process(CommitCheckModel item) throws Exception {
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        String sinceTime = (String) jobParameters.get("sinceTime");

        List commitHistory = commitHistoryApiAdapter.body(item, sinceTime);

        if (commitHistory.isEmpty()) {
            item.didntCommit();
        } else {
            item.didCommit();
        }

        log.info("사용자:{} {}일 커밋 여부:{} ", item.getGithubName(), sinceTime, item.getDone());

        return item;
    }
}
