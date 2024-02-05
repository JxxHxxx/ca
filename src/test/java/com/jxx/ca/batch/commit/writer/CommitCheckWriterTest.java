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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
    EntityManager entityManager;

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
                true,
                "newRepo2",
                "jxxHxxx",
                true);
        // 사전 검증
        assertThat(todayCommit1.getDone()).isFalse();
        assertThat(todayCommit1.getRecentlyPushedRepoName()).isEqualTo("legacyRepo1");
        assertThat(todayCommit1.getCommitDoneCheckTime()).isNull();

        //when
        itemWriter.write(List.of(commitCheckModel1, commitCheckModel2));

        TodayCommit updatedTodayCommit = todayCommitRepository.findById(todayCommit1.getPk()).get();
        assertThat(updatedTodayCommit.getDone()).isTrue();
        assertThat(updatedTodayCommit.getRecentlyPushedRepoName()).isEqualTo("newRepo1");
        assertThat(updatedTodayCommit.getCommitDoneCheckTime()).isNotNull();
    }
}