package com.jxx.ca.domain;

import com.jxx.ca.github.authorization.TokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class GithubRecentRepoFinderFunction implements Function<String, String> {

    private final TokenGenerator tokenGenerator;

    @Override
    public String apply(String githubMemberName) {
        log.info("githubMemberName = {}", githubMemberName);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, tokenGenerator.receive());
        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        List result;
        try {
            ResponseEntity<List> recentRepoEntity = restTemplate.exchange(
                    "https://api.github.com/users/{username}/repos?sort=pushed&page=1&per_page=1",
                    HttpMethod.GET, entity, List.class,
                    githubMemberName);

            result = recentRepoEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.info("response status {} reason {}",e.getStatusCode(), e.getStatusCode().getReasonPhrase(),  e);
            return null;
        }

        if (result.isEmpty()) {
            log.info("리포짓토리가 존재하지 않습니다.");
            return null;
        }

        Map<String, Object> body = (Map<String, Object>) result.get(0);
        return (String) body.get("name");
    }
}
