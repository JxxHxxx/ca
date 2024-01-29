package com.jxx.ca.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


class TodayCommitRenewLauncherTest {

    List<GithubMember> githubMembers;

    @BeforeEach
    void settings() {
        GithubMember githubMember = new GithubMember("xuni");
        new TodayCommit(githubMember, "legacyRepo");

        githubMembers = List.of(
                new GithubMember("JxxHxxx"),
                new GithubMember("fredlee613"),
                githubMember);
    }

    @Test
    void initialize_today_commit_renew_launcher() {
        TodayCommitRenewLauncher todayCommitRenewLauncher = new TodayCommitRenewLauncher(githubMembers);
        List<GithubMember> existingMembers = todayCommitRenewLauncher.getExistingMembers();
        assertThat(existingMembers.size()).isEqualTo(1);

        List<GithubMember> newMembers = todayCommitRenewLauncher.getNewMembers();
        assertThat(newMembers.size()).isEqualTo(2);
    }

    @Test
    void enroll_repo_name(){
        TodayCommitRenewLauncher todayCommitRenewLauncher = new TodayCommitRenewLauncher(githubMembers);
        //when
        List<TodayCommit> todayCommits = todayCommitRenewLauncher.enrollRepoName(githubName -> "repo" + githubName);

        List<String> repoNames = todayCommits
                .stream()
                .map(todayCommit -> todayCommit.getRecentlyPushedRepoName())
                .toList();

        assertThat(repoNames).contains("repoJxxHxxx", "repofredlee613");
    }

    @Test
    void renew() {
        TodayCommitRenewLauncher todayCommitRenewLauncher = new TodayCommitRenewLauncher(githubMembers);
        List<GithubMember> existingMembers = todayCommitRenewLauncher.getExistingMembers();

        String legacyRepoName = existingMembers.get(0).getTodayCommit().getRecentlyPushedRepoName();
        assertThat(legacyRepoName).isEqualTo("legacyRepo");
        //when
        todayCommitRenewLauncher.renewRepoName(githubName -> "renew" + githubName);

        String renewReoName = existingMembers.get(0).getTodayCommit().getRecentlyPushedRepoName();
        assertThat(renewReoName).isEqualTo("renewxuni");
    }
}