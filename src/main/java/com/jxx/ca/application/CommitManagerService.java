package com.jxx.ca.application;

import com.jxx.ca.domain.GithubMember;
import com.jxx.ca.domain.GithubRecentRepoFinderFunction;
import com.jxx.ca.domain.TodayCommitTracer;
import com.jxx.ca.domain.TodayCommit;
import com.jxx.ca.github.api.CommitHistoryApiAdapter;
import com.jxx.ca.github.authorization.TokenGenerator;
import com.jxx.ca.infra.GithubMemberRepository;
import com.jxx.ca.infra.TodayCommitRepository;
import com.jxx.ca.dto.request.UserEnrollForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommitManagerService {

    private final GithubMemberRepository githubMemberRepository;
    private final TodayCommitRepository todayCommitRepository;
    private final CommitHistoryApiAdapter commitHistoryApiAdapter;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public void enrollMembers(List<UserEnrollForm> forms) {
        List<GithubMember> githubMembers = forms.stream()
                .map(form -> new GithubMember(form.githubName()))
                .toList();

        List<GithubMember> savedGithubMembers = githubMemberRepository.saveAll(githubMembers);

        TodayCommitTracer todayCommitTracer = new TodayCommitTracer(savedGithubMembers);

        List<TodayCommit> todayCommits = todayCommitTracer.createTodayCommit(new GithubRecentRepoFinderFunction(tokenGenerator));
        todayCommitRepository.saveAll(todayCommits);
    }
    @Transactional
    public void renewRepoNameAllUsers() {
        List<GithubMember> githubMembers = githubMemberRepository.findAll();

        TodayCommitTracer todayCommitTracer = new TodayCommitTracer(githubMembers);
        GithubRecentRepoFinderFunction repoFindFunction = new GithubRecentRepoFinderFunction(tokenGenerator);

        // 기존 사용자 repoName update - dirty checking
        todayCommitTracer.renewTodayCommit(repoFindFunction);

        // 신규 사용자 repoName update
        List<TodayCommit> todayCommits = todayCommitTracer.createTodayCommit(repoFindFunction);
        todayCommitRepository.saveAll(todayCommits);
    }
    @Transactional
    public void checkTodayCommit(String sinceTime) {
        List<TodayCommit> todayCommits = todayCommitRepository.findAll();

        for (TodayCommit todayCommit : todayCommits) {
            List commitHistory = commitHistoryApiAdapter.getResponseBody(todayCommit.getGithubMember().getGithubName(),
                    todayCommit.getRecentlyPushedRepoName(), sinceTime);

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
