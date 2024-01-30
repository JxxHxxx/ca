package com.jxx.ca.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class GithubRecentRepoFinderFunction implements Function<String, String> {

    @Override
    public String apply(String githubMemberName) {
        log.info("githubMemberName = {}", githubMemberName);
        RestTemplate restTemplate = new RestTemplate();

        List result;
        try {
            result = restTemplate.getForObject("https://api.github.com/users/{username}/repos?" +
                    "sort=pushed&page=1&per_page=1", List.class, githubMemberName);
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
