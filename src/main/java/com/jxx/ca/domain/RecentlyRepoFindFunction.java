package com.jxx.ca.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class RecentlyRepoFindFunction implements Function<String, String> {

    @Override
    public String apply(String githubMemberName) {
        log.info("githubMemberName = {}", githubMemberName);
        RestTemplate restTemplate = new RestTemplate();
        List result = restTemplate.getForObject("https://api.github.com/users/{username}/repos?" +
                "sort=pushed&page=1&per_page=1", List.class, githubMemberName);

        // 레포짓토리가 없을 때 처리
        if (result.isEmpty()) {
            return null;
        }

        Map<String, Object> body = (Map<String, Object>) result.get(0);
        return (String) body.get("name");
    }
}
