package com.jxx.ca;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MemoryCommitRepository memoryCommitRepository;

    // 알림을 받기 위한 등록
    @PostMapping()
    public void enroll() {

    }

    @GetMapping("/users/{username}")
    public ResponseEntity<String> findRecentlyPushedRepo(@PathVariable("username") String username) {
        RestTemplate restTemplate = new RestTemplate();

        List result = restTemplate.getForObject("https://api.github.com/users/{username}/repos?" +
                "sort=pushed&page=1&per_page=1", List.class, username);
        // 없는 아이디로 조회했을 경우 고려

        if (result.isEmpty()) {
            log.info("username = {} has no repository", username);
            return ResponseEntity.ok(null);
        }

        Map<String, Object> body = (Map<String, Object>) result.get(0);
        String repoName =(String) body.get("name");

        memoryCommitRepository.save(username, repoName);
        log.info("username = {} find repository {}", username, repoName);

        return ResponseEntity.ok(repoName);
    }

    @GetMapping("/users/{username}/repos/{reponame}/check")
    public void checkCommit(@PathVariable("username") String uesrname, @PathVariable("reponame") String reponame,
                            @RequestParam("since") String sinceTime) {
        RestTemplate restTemplate = new RestTemplate();
        List results = restTemplate.getForObject(
                "https://api.github.com/repos/{username}/{reponame}/commits?since={sinceTime}",
                List.class, uesrname, reponame, sinceTime);
//        2024-01-21T15:00:00Z
        if (results.isEmpty()) {
            log.info("오늘 커밋 안했어요");
        } else {
            log.info("오늘 커밋 했어요.");
        }
    }

}
