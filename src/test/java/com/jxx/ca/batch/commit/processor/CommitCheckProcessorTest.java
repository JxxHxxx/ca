package com.jxx.ca.batch.commit.processor;


import com.jxx.ca.batch.commit.model.CommitCheckModel;
import com.jxx.ca.github.api.CommitHistoryApiAdapter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@SpringBatchTest
class CommitCheckProcessorTest {

    CommitHistoryApiAdapter<CommitCheckModel> commitHistoryApiAdapter;
    CommitCheckProcessor commitCheckProcessor;;

    @BeforeEach
    void given() {
        // githubName 이 jxxHxxx 이면 빈 리스트, 비어있지 않은 리스트를 반환한다.
        commitHistoryApiAdapter = (commitCheckModel, sinceTime) ->
                commitCheckModel.getGithubName().equals("jxxHxxx") ? List.of() : List.of(commitCheckModel);

        commitCheckProcessor = new CommitCheckProcessor(commitHistoryApiAdapter);
    }

    /**
     * ref : SimpleCommitHistoryApiAdapter.java 여기에 있는 GITHUB API 참고
     */
    @DisplayName("commitCheckProcessor 는 GITHUB API 와 협력한다. " +
            "GITHUB API sinceTime을 기준으로 당일 커밋 내역이 있는지 확인한다. " +
            "없다면 CommitCheckModel done 필드를 false 로 변경한다. ")
    @Test
    void pass_commit_check_process() throws Exception {
        CommitCheckModel commitCheckModel = new CommitCheckModel(
                1l,
                LocalDate.now(),
                LocalDateTime.now(),
                true,
                "xuniName",
                "xuni",
                true);

        CommitCheckModel passedCommitCheckModel = commitCheckProcessor.process(commitCheckModel);

        assertThat(passedCommitCheckModel.getDone()).isTrue();
    }

    @DisplayName("commitCheckProcessor 는 GITHUB API 와 협력한다. " +
            "GITHUB API sinceTime을 기준으로 당일 커밋 내역이 있는지 확인한다. " +
            "있다면 CommitCheckModel done 필드를 true 로 변경한다. ")
    @Test
    void not_pass_commit_check_process() throws Exception {
        CommitCheckModel commitCheckModel = new CommitCheckModel(
                1l,
                LocalDate.now(),
                LocalDateTime.now(),
                true,
                "jxxHxxName",
                "jxxHxxx",
                true);
        CommitCheckModel processedCommitCheckModel = commitCheckProcessor.process(commitCheckModel);

        assertThat(processedCommitCheckModel.getDone()).isFalse();
    }
}