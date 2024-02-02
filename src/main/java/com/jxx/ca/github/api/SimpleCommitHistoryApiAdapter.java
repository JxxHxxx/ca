package com.jxx.ca.github.api;

import com.jxx.ca.batch.commit.reader.CommitCheckModel;
import com.jxx.ca.github.authorization.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SimpleCommitHistoryApiAdapter implements CommitHistoryApiAdapter<CommitCheckModel> {

    private final TokenGenerator tokenGenerator;

    @Override
    public List body(CommitCheckModel commitCheckModel, String sinceTime) {

        HttpEntity<String> entity = setRequestEntity();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> commitHistoryEntity = restTemplate.exchange(
                "https://api.github.com/repos/{username}/{reponame}/commits?since={sinceTime}",
                HttpMethod.GET, entity, List.class,
                commitCheckModel.getGithubName(), commitCheckModel.getRecentlyPushedRepoName(), sinceTime);

        return commitHistoryEntity.getBody();
    }

    private HttpEntity<String> setRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, tokenGenerator.receive());
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        return entity;
    }
}
