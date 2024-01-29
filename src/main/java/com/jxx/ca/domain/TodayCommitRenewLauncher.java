package com.jxx.ca.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class TodayCommitRenewLauncher {

    private List<GithubMember> existingMembers = new ArrayList<>();
    private List<GithubMember> newMembers = new ArrayList<>();

    public TodayCommitRenewLauncher(List<GithubMember> githubMembers) {
        filterMembers(githubMembers);
    }

    private void filterMembers(List<GithubMember> githubMembers) {
        for (GithubMember githubMember : githubMembers) {
            if (githubMember.hasTodayCommit()) {
                existingMembers.add(githubMember);
            } else {
                newMembers.add(githubMember);
            }
        }
    }

    public List<TodayCommit> enrollRepoName(Function<String, String> function) {
        return newMembers.stream()
                .map(newMember -> new TodayCommit(newMember,
                        function.apply(newMember.getGithubName())))
                .toList();
    }

    public void renewRepoName(Function<String, String> function) {
        existingMembers.forEach(existingMember -> {
            TodayCommit todayCommit = existingMember.getTodayCommit();
            String repoName = function.apply(existingMember.getGithubName());
            todayCommit.updateRecentlyPushedRepoName(repoName);
        });
    }
}
