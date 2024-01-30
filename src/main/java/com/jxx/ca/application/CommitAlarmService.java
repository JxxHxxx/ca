package com.jxx.ca.application;

import com.jxx.ca.domain.GithubMember;
import com.jxx.ca.domain.GithubRecentRepoFinderFunction;
import com.jxx.ca.domain.TodayCommitTracer;
import com.jxx.ca.domain.TodayCommit;
import com.jxx.ca.infra.GithubMemberRepository;
import com.jxx.ca.infra.TodayCommitRepository;
import com.jxx.ca.dto.request.UserEnrollForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
    @Transactional
    public void renewRepoName() {
        List<GithubMember> githubMembers = githubMemberRepository.findAll();

        TodayCommitTracer todayCommitTracer = new TodayCommitTracer(githubMembers);
        GithubRecentRepoFinderFunction repoFindFunction = new GithubRecentRepoFinderFunction();

        // 기존 사용자 repoName update
        todayCommitTracer.renewTodayCommit(repoFindFunction);

        // 신규 사용자 repoName update
        List<TodayCommit> todayCommits = todayCommitTracer.createTodayCommit(repoFindFunction);
        todayCommitRepository.saveAll(todayCommits);
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
