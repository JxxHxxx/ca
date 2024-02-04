package com.jxx.ca.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;


class TodayCommitTracerTest {

    List<GithubMember> githubMembers;

    /**
     * TodayCommitTrace's 책임
     * GithubMember 의 TodayCommit 을 추적(CREATE, UPDATE)한다.
     *
     * 이를 위해 TodayCommitTracer 는
     * GithubMember을 TodayCommit 존재 여부로 두 그룹으로 나눈다.
     *
     * - existingMembers : TodayCommit 엔티티가 존재하는 GithubMember, 이전 시점에 시스템에 의해 repository 를 탐색당한 적이 있는 GithubMember
     * 편의 상, 기존 멤버라 부르겠다.
     *
     * - newMembers : TodayCommit 엔티티가 존재하지 않는 GithubMember, 이전 시점에 시스템에 의해 repository 를 탐색당한 없는 GithubMember
     * 편의 상, 신규 멤버라 부르겠다.
     */

    @BeforeEach
    void initData() {
        // existingMembers 생성 작업
        GithubMember existMember = new GithubMember("xuni");
        new TodayCommit(existMember, "legacyRepo");

        // newMembers 생성 작업
        GithubMember newMember1 = new GithubMember("JxxHxxx");
        GithubMember newMember2 = new GithubMember("fredlee613");

        githubMembers = List.of(existMember, newMember1, newMember2);
    }

    @DisplayName("TodayCommitTracer 객체에 githubMembers 를 인자로 받으면 초기화 시," +
            "existingMembers, newMembers 를 구분하여 필드에 주입힌다." +
            "결과적으로 기존 멤버 1명, 신규 멤버 2명이다.")
    @Test
    void initialize_today_commit_renew_launcher() {
        //given - when
        TodayCommitTracer todayCommitTracer = new TodayCommitTracer(githubMembers);

        // then
        List<GithubMember> existingMembers = todayCommitTracer.getExistingMembers();
        assertThat(existingMembers.size()).isEqualTo(1);
        assertThat(existingMembers).extracting("githubName").containsExactly("xuni");

        // then
        List<GithubMember> newMembers = todayCommitTracer.getNewMembers();
        assertThat(newMembers.size()).isEqualTo(2);
        assertThat(newMembers).extracting("githubName").containsExactly("JxxHxxx", "fredlee613");

    }

    @DisplayName("TodayCommit 추적을 위해서는 GITHUB API 를 사용해야 한다. 그러나 GITHUB API RATE LIMIT 이 존재하므로 테스트 시에는 " +
            "인터페이스를 직접 구현하여 기능을 점검하도록 하겠다. " +
            "실제로는 createTodayCommit 메서드는 가장 최근 업데이트된 repositoryName 을 리턴 받아야 한다." +
            "" +
            "검증 : createTodayCommit 호출 시, 신규 멤버의 TodayCommit 이 CREATE 된다.")
    @Test
    void create_today_commit(){
        //given
        TodayCommitTracer todayCommitTracer = new TodayCommitTracer(githubMembers);

        List<TodayCommit> todayCommits = todayCommitTracer.getNewMembers().stream()
                .map(newMember -> newMember.getTodayCommit())
                .filter(Objects::nonNull)
                .toList();

        // 신규 멤버는 TodayCommit 이 존재하지 않음
        assertThat(todayCommits).isEmpty();

        List<TodayCommit> createdTodayCommits = todayCommitTracer.createTodayCommit(githubName -> "repo" + githubName);

        List<String> repoNames = createdTodayCommits
                .stream()
                .map(todayCommit -> todayCommit.getRecentlyPushedRepoName())
                .toList();

        // then - 신규 멤버의 TodayCommit.recentlyPushedRepoName 확인
        assertThat(repoNames).containsExactly("repoJxxHxxx", "repofredlee613");

    }

    @DisplayName("TodayCommit 추적을 위해서는 GITHUB API 를 사용해야 한다. 그러나 GITHUB API RATE LIMIT 이 존재하므로 테스트 시에는 " +
            "인터페이스를 직접 구현하여 기능을 점검하도록 하겠다. " +
            "실제로는 createTodayCommit 메서드는 가장 최근 업데이트된 repositoryName 을 리턴 받아야 한다." +
            "" +
            "검증 : renewTodayCommit 호출 시, 기존 멤버의 TodayCommit 이 UPDATE 된다.")
    @Test
    void renew() {
        TodayCommitTracer todayCommitTracer = new TodayCommitTracer(githubMembers);
        List<GithubMember> existingMembers = todayCommitTracer.getExistingMembers();

        String legacyRepoName = existingMembers.get(0).getTodayCommit().getRecentlyPushedRepoName();
        assertThat(legacyRepoName).isEqualTo("legacyRepo");
        //when - renewTodayCommit 호출을 통해 가장 최근 업데이트 된 repositoryName 갱신 작업
        todayCommitTracer.renewTodayCommit(githubName -> "renew" + githubName);

        String renewReoName = existingMembers.get(0).getTodayCommit().getRecentlyPushedRepoName();
        assertThat(renewReoName).isEqualTo("renewxuni");
    }
}