package com.jxx.ca;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommitService {

    private final MemoryCommitRepositoryV2 memoryCommitRepository;

    public void notification() {
        List<Member> members = memoryCommitRepository.getAll();
        for (Member member : members) {
            Boolean todayCommit = member.getTodayCommit();
            if (todayCommit) {
                log.info("username = {} 커밋 했습니다.", member.getName());
            } else {
                log.info("username = {} 아직 커밋하지 않았습니다.", member.getName());
            }
        }
    }

    public void checkTodayCommit(String sinceTime) {
        RestTemplate restTemplate = new RestTemplate();

        List<Member> members = memoryCommitRepository.getAll();
        for (Member member : members) {
            List commitHistory = restTemplate.getForObject(
                    "https://api.github.com/repos/{username}/{reponame}/commits?since={sinceTime}",
                    List.class, member.getName(), member.getLastUpdateRepoName(), sinceTime);

            if (commitHistory.isEmpty()) {
                member.setTodayCommit(false);
                memoryCommitRepository.update(member);
            }
            else {
                member.setTodayCommit(true);
                memoryCommitRepository.update(member);
            }
        }
    }
}
