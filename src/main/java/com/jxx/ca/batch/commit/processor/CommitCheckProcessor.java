package com.jxx.ca.batch.commit.processor;

import com.jxx.ca.batch.commit.reader.CommitCheckModel;
import com.jxx.ca.github.authorization.TokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CommitCheckProcessor implements ItemProcessor<CommitCheckModel, CommitCheckModel> {

    private final TokenGenerator tokenGenerator;

    @Override
    public CommitCheckModel process(CommitCheckModel item) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> jobParameters = StepSynchronizationManager.getContext().getJobParameters();
        String sinceTime = (String) jobParameters.get("sinceTime");

        HttpEntity<String> entity = setRequestEntity();

        ResponseEntity<List> commitHistoryEntity = restTemplate.exchange(
                "https://api.github.com/repos/{username}/{reponame}/commits?since={sinceTime}",
                HttpMethod.GET, entity, List.class,
                item.getGithubName(), item.getRecentlyPushedRepoName(), sinceTime);

        List commitHistory = commitHistoryEntity.getBody();

        if (commitHistory.isEmpty()) {
            item.didntCommit();
        } else {
            item.didCommit();
        }

        log.info("사용자:{} {}일 커밋 여부:{} ", item.getGithubName(), sinceTime, item.getDone());

        return item;
    }

    private HttpEntity<String> setRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, tokenGenerator.receive());
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        return entity;
    }
}
