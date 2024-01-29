package com.jxx.ca.application;

import com.jxx.ca.domain.GithubMember;
import com.jxx.ca.domain.RecentlyRepoFindFunction;
import com.jxx.ca.domain.TodayCommitRenewLauncher;
import com.jxx.ca.domain.TodayCommit;
import com.jxx.ca.infra.GithubMemberRepository;
import com.jxx.ca.infra.TodayCommitRepository;
import com.jxx.ca.dto.request.UserEnrollForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

    @Transactional
    public void enrollMembers(List<UserEnrollForm> forms) {
        List<GithubMember> githubMembers = forms.stream()
                .map(form -> new GithubMember(form.githubName()))
                .toList();

        githubMemberRepository.saveAll(githubMembers);
    }

    // 배치성 작업 - 여기 로직 잘못됨

    /**
     * 신규 사용자는 TodayCommit 테이블 레코드가 생성되어야 하고
     * 그외 사용자는 레코드가 업데이트 되어야 함
     */
    public void searchRecentlyPushedRepo() {
        List<GithubMember> githubMembers = githubMemberRepository.findAll();
        // TodayCommit 이 있는지 확인

        List<TodayCommit> todayCommits = new ArrayList<>();
        for (GithubMember githubMember : githubMembers) {
            String recentlyRepoName = receiveRecentlyRepoName(githubMember.getGithubName());
            TodayCommit todayCommit = new TodayCommit(githubMember, recentlyRepoName);
            todayCommits.add(todayCommit);
        }
        todayCommitRepository.saveAll(todayCommits);
    }

    @Transactional
    public void renewRepoName() {
        List<GithubMember> githubMembers = githubMemberRepository.findAll();

        TodayCommitRenewLauncher todayCommitRenewLauncher = new TodayCommitRenewLauncher(githubMembers);
        RecentlyRepoFindFunction repoFindFunction = new RecentlyRepoFindFunction();
        todayCommitRenewLauncher.renewRepoName(repoFindFunction);

        List<TodayCommit> todayCommits = todayCommitRenewLauncher.enrollRepoName(repoFindFunction);
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
                todayCommit.checkCommitDone(false);
            } else {
                todayCommit.checkCommitDone(true);
            }
        }
    }

    public void notificate() {
        List<TodayCommit> todayCommits = todayCommitRepository.findAll();

    }


}
