package com.jxx.ca.github.api;

import com.jxx.ca.github.authorization.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SimpleCommitHistoryApiAdapter implements CommitHistoryApiAdapter {
    private final TokenGenerator tokenGenerator;
    private static final Integer INTERVAL_TIME = 9;

    @Override
    public List getResponseBody(String username, String repoName, String sinceTime) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> commitHistoryEntity = restTemplate.exchange(
                "https://api.github.com/repos/{username}/{reponame}/commits?since={sinceTime}",
                HttpMethod.GET, setRequestEntity(), List.class, username, repoName, adjustSinceTime(sinceTime));

        return commitHistoryEntity.getBody();
    }

    /**
     * github api server 는 UTC 타임존을 따르기 때문에 한국의 경우 -9시간을 해야 적절하다.
     * 아래 private 메서드는 이를 조절해주는 기능이다.
     * /api.github.com/repos/{username}/{reponame}/commits?since={sinceTime}
     * sinceTime = 2024-02-16T00:00:00Z -> 2024-02-15T15:00Z 로 변경된다.
     */
    protected String adjustSinceTime(String sinceTime) {
        Instant instant = Instant.parse(sinceTime);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);
        ZonedDateTime afterTime = zonedDateTime.minusHours(INTERVAL_TIME);

        return String.valueOf(afterTime);
    }

    private HttpEntity<String> setRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, tokenGenerator.receive());
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        return entity;
    }
}
