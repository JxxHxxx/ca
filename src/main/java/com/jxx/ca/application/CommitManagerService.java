package com.jxx.ca.application;

import com.jxx.ca.domain.GithubMember;
import com.jxx.ca.domain.GithubRecentRepoFinderFunction;
import com.jxx.ca.domain.TodayCommitTracer;
import com.jxx.ca.domain.TodayCommit;
import com.jxx.ca.dto.response.TodayCommitInformation;
import com.jxx.ca.dto.response.TodayCommitResponse;
import com.jxx.ca.github.api.GithubRepoCommitHistoryApiAdapter;
import com.jxx.ca.github.authorization.TokenGenerator;
import com.jxx.ca.infra.GithubMemberRepository;
import com.jxx.ca.infra.TodayCommitRepository;
import com.jxx.ca.dto.request.UserEnrollForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommitManagerService {

    private final GithubMemberRepository githubMemberRepository;
    private final TodayCommitRepository todayCommitRepository;
    private final GithubRepoCommitHistoryApiAdapter githubRepoCommitHistoryApiAdapter;
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
    public void checkTodayCommits(String sinceTime) {
        List<TodayCommit> todayCommits = todayCommitRepository.findAll();
        todayCommits.forEach(todayCommit -> changeCommitDone(sinceTime, todayCommit));
    }

    @Transactional
    public void checkTodayCommit(Long todayCommitPk, String sinceTime) {
        TodayCommit todayCommit = todayCommitRepository.findById(todayCommitPk).orElseThrow(() -> new IllegalArgumentException());
        changeCommitDone(sinceTime, todayCommit);
    }

    private void changeCommitDone(String sinceTime, TodayCommit todayCommit) {
        List commitHistory = githubRepoCommitHistoryApiAdapter.request(todayCommit.getGithubMember().getGithubName(),
                todayCommit.getRecentlyPushedRepoName(), sinceTime);

        if (commitHistory.isEmpty()) {
            todayCommit.checkCommitDone(false);
        } else {
            todayCommit.checkCommitDone(true);
        }
    }

    public void notificate() {
        List<TodayCommit> todayCommits = todayCommitRepository.findAll();

    }


    public List<TodayCommitResponse> findTodayCommits() {
        List<TodayCommit> todayCommits = todayCommitRepository.findAll();

        return todayCommits.stream()
                .map(todayCommit -> {
                    String done = todayCommit.getDone() ? "O" : "X";
                    return new TodayCommitResponse(todayCommit.getGithubMember().getGithubName(), done);
                })
                .toList();
    }

    public List<TodayCommitResponse> findTodayCommit(String namePattern) {
        if (Objects.isNull(namePattern) || namePattern.isBlank()) {
            return findTodayCommits();
        }
        List<TodayCommitInformation> informations = todayCommitRepository.receiveTodayCommitInfoByGithubName(namePattern);

        return informations.stream()
                .map(info -> {
                    String done = info.isDone() ? "O" : "X";
                    return new TodayCommitResponse(info.getGithubName(), done);
                })
                .toList();
    }
}
