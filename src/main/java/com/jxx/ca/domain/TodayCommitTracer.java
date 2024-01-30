package com.jxx.ca.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Getter
public class TodayCommitTracer {

    private List<GithubMember> existingMembers = new ArrayList<>();
    private List<GithubMember> newMembers = new ArrayList<>();

    public TodayCommitTracer(List<GithubMember> githubMembers) {
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

    public List<TodayCommit> createTodayCommit(Function<String, String> function) {
        return newMembers.stream()
                .map(newMember -> {
                    String repoName = function.apply(newMember.getGithubName());
                    return repoName == null ? null : new TodayCommit(newMember, repoName);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public void renewTodayCommit(Function<String, String> function) {
        existingMembers.forEach(existingMember -> {
            TodayCommit todayCommit = existingMember.getTodayCommit();
            String repoName = function.apply(existingMember.getGithubName());
            if (repoName != null) {
                todayCommit.updateRecentlyPushedRepoName(repoName);
            }
        });
    }
}
