package com.jxx.ca.batch.commit.writer;

import com.jxx.ca.batch.commit.reader.CommitCheckModel;
import com.jxx.ca.domain.GithubMember;
import com.jxx.ca.domain.TodayCommit;
import com.jxx.ca.infra.GithubMemberRepository;
import com.jxx.ca.infra.TodayCommitRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@Slf4j
@SpringBootTest
@SpringBatchTest
class CommitCheckWriterTest {

    @Autowired
    @Qualifier(value = "commitCheckWriter")
    JdbcBatchItemWriter<CommitCheckModel> itemWriter;
    @Autowired
    GithubMemberRepository githubMemberRepository;
    @Autowired
    TodayCommitRepository todayCommitRepository;

    TodayCommit todayCommit1;
    TodayCommit todayCommit2;

    @BeforeEach
    void init() {
        GithubMember githubMember1 = new GithubMember("jxxHxxx");
        GithubMember githubMember2 = new GithubMember("xuni");
        githubMemberRepository.saveAll(List.of(githubMember1, githubMember2));
        todayCommit1 = todayCommitRepository.save(new TodayCommit(githubMember1,"legacyRepo1"));
        todayCommit2 = todayCommitRepository.save(new TodayCommit(githubMember2,"legacyRepo2"));
    }

    @AfterEach
    void afterEach() {
        githubMemberRepository.deleteAll();
        todayCommitRepository.deleteAll();
    }

    @Test
    void write() throws Exception {
        //given
        CommitCheckModel commitCheckModel1 = new CommitCheckModel(
                todayCommit1.getPk(),
                LocalDate.now(),
                LocalDateTime.now(),
                true,
                "newRepo1",
                "jxxHxxx",
                true);

        CommitCheckModel commitCheckModel2 = new CommitCheckModel(
                todayCommit2.getPk(),
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                "newRepo2",
                "xuni",
                true);
        // 사전 검증
        assertThat(todayCommit1.getDone()).isFalse();
        assertThat(todayCommit1.getRecentlyPushedRepoName()).isEqualTo("legacyRepo1");
        assertThat(todayCommit1.getCommitDoneCheckTime()).isNull();

        //when
        itemWriter.write(List.of(commitCheckModel1, commitCheckModel2));

        TodayCommit updatedTodayCommit1 = todayCommitRepository.findById(todayCommit1.getPk()).get();
        assertThat(updatedTodayCommit1.getDone()).isTrue();
        assertThat(updatedTodayCommit1.getRecentlyPushedRepoName()).isEqualTo("newRepo1");
        assertThat(updatedTodayCommit1.getCommitDoneCheckTime()).isNotNull();

        TodayCommit updatedTodayCommit2 = todayCommitRepository.findById(todayCommit2.getPk()).get();
        assertThat(updatedTodayCommit2.getDone()).isFalse();
        assertThat(updatedTodayCommit2.getRecentlyPushedRepoName()).isEqualTo("newRepo2");
        assertThat(updatedTodayCommit2.getCommitDoneCheckTime()).isNotNull();


    }
}