package com.jxx.ca.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


class TodayCommitTracerTest {

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
        TodayCommitTracer todayCommitTracer = new TodayCommitTracer(githubMembers);
        List<GithubMember> existingMembers = todayCommitTracer.getExistingMembers();
        assertThat(existingMembers.size()).isEqualTo(1);

        List<GithubMember> newMembers = todayCommitTracer.getNewMembers();
        assertThat(newMembers.size()).isEqualTo(2);
    }

    @Test
    void enroll_repo_name(){
        TodayCommitTracer todayCommitTracer = new TodayCommitTracer(githubMembers);
        //when
        List<TodayCommit> todayCommits = todayCommitTracer.createTodayCommit(githubName -> "repo" + githubName);

        List<String> repoNames = todayCommits
                .stream()
                .map(todayCommit -> todayCommit.getRecentlyPushedRepoName())
                .toList();

        assertThat(repoNames).contains("repoJxxHxxx", "repofredlee613");
    }

    @Test
    void renew() {
        TodayCommitTracer todayCommitTracer = new TodayCommitTracer(githubMembers);
        List<GithubMember> existingMembers = todayCommitTracer.getExistingMembers();

        String legacyRepoName = existingMembers.get(0).getTodayCommit().getRecentlyPushedRepoName();
        assertThat(legacyRepoName).isEqualTo("legacyRepo");
        //when
        todayCommitTracer.renewTodayCommit(githubName -> "renew" + githubName);

        String renewReoName = existingMembers.get(0).getTodayCommit().getRecentlyPushedRepoName();
        assertThat(renewReoName).isEqualTo("renewxuni");
    }
}