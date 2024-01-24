package com.jxx.ca.application;

import com.jxx.ca.domain.GithubMember;
import com.jxx.ca.domain.TodayCommit;
import com.jxx.ca.infra.GithubMemberRepository;
import com.jxx.ca.infra.TodayCommitRepository;
import com.jxx.cadto.request.UserEnrollForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommitAlarmService {

    private final GithubMemberRepository githubMemberRepository;
    private final TodayCommitRepository todayCommitRepository;

    @Transactional
    public void enrollMember(UserEnrollForm form) {
        GithubMember githubMember = new GithubMember(form.githubName());
        githubMemberRepository.save(githubMember);
    }

    // 배치성 작업
    public void searchRecentlyPushedRepo() {
        List<GithubMember> githubMembers = githubMemberRepository.findAll();

        List<TodayCommit> todayCommits = new ArrayList<>();
        for (GithubMember githubMember : githubMembers) {
            String recentlyRepoName = receiveRecentlyRepoName(githubMember.getGithubName());
            TodayCommit todayCommit = new TodayCommit(githubMember, LocalDate.now(), recentlyRepoName);
            todayCommits.add(todayCommit);
        }
        todayCommitRepository.saveAll(todayCommits);
    }

    private String receiveRecentlyRepoName(String githubMemberName) {
        RestTemplate restTemplate = new RestTemplate();
        List result = restTemplate.getForObject("https://api.github.com/users/{username}/repos?" +
                "sort=pushed&page=1&per_page=1", List.class, githubMemberName);

        // 없는 사용자 처리

        // 레포짓토리가 없을 때 처리
        if (result.isEmpty()) {
            return null;
        }

        Map<String, Object> body = (Map<String, Object>) result.get(0);
        return (String) body.get("name");
    }

    @Transactional
    public void checkTodayCommit(final String sinceTime) {
        RestTemplate restTemplate = new RestTemplate();
        List<TodayCommit> todayCommits = todayCommitRepository.findAll();
        for (TodayCommit todayCommit : todayCommits) {
            List commitHistory = restTemplate.getForObject(
                    "https://api.github.com/repos/{username}/{reponame}/commits?since={sinceTime}",
                    List.class, todayCommit.getGithubMember().getGithubName(), todayCommit.getRecentlyPushedRepoName(), sinceTime);

            if (commitHistory.isEmpty()) {
                todayCommit.setDone(false);
            }
            else {
                todayCommit.setDone(true);
            }
        }
    }

    public void notificate() {
        List<TodayCommit> todayCommits = todayCommitRepository.findAll();

    }


}
