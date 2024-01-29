package com.jxx.ca.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class GithubMemberTest {

    @Test
    void nullTest() {
        GithubMember githubMember = new GithubMember("jxxHxxx");
        TodayCommit todayCommit = githubMember.getTodayCommit();
        Assertions.assertThat(todayCommit).isNull();
    }

}