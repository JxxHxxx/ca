package com.jxx.ca.batch.commit.reader;

import com.jxx.ca.domain.GithubMember;
import com.jxx.ca.domain.TodayCommit;
import com.jxx.ca.infra.GithubMemberRepository;
import com.jxx.ca.infra.TodayCommitRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@SpringBatchTest
class CommitReaderTest {

    @Autowired
    ItemStreamReader<CommitCheckModel> reader; // itemReader 는 open 메서드가 없음
    @Autowired
    TodayCommitRepository todayCommitRepository;
    @Autowired
    GithubMemberRepository githubMemberRepository;

    public StepExecution getStepExecution() {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.getExecutionContext().putString("id", "test-execute-1");
        return execution;
    }
    @BeforeEach
    void init() {
        GithubMember githubMember1 = githubMemberRepository.save(new GithubMember("jxxHxxx"));
        GithubMember githubMember2 = githubMemberRepository.save(new GithubMember("xuni"));
        GithubMember githubMember3 = githubMemberRepository.save(new GithubMember("kaki"));

        githubMemberRepository.saveAll(List.of(githubMember1, githubMember2, githubMember3));

        todayCommitRepository.saveAll(List.of(
                new TodayCommit(githubMember1, "jxxHxxRepo"),
                new TodayCommit(githubMember2, "xuniRepo"),
                new TodayCommit(githubMember3, "kakiRepo"))
        );

        reader.open(getStepExecution().getExecutionContext()); // read 전에 열어야 됨
    }

    @AfterEach
    void afterEach() {
        githubMemberRepository.deleteAll();
        todayCommitRepository.deleteAll();
    }

    @Test
    void read() throws Exception {
        List<CommitCheckModel> commitCheckModels = new ArrayList<>();
        while (true) {
            CommitCheckModel model = reader.read();
            if (model == null) {
                break;
            }
            commitCheckModels.add(model);
        }

        assertThat(commitCheckModels).extracting("recentlyPushedRepoName", "githubName")
                .contains(
                        tuple("jxxHxxRepo", "jxxHxxx"),
                        tuple("xuniRepo", "xuni"),
                        tuple("kakiRepo", "kaki"));
    }
}