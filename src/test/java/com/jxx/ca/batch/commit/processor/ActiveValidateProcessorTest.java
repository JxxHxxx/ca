package com.jxx.ca.batch.commit.processor;

import com.jxx.ca.batch.commit.model.CommitCheckModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@SpringBatchTest
class ActiveValidateProcessorTest {
    ActiveValidateProcessor activeValidateProcessor = new ActiveValidateProcessor();

    @DisplayName("CommitCheckModel 객체 필드 active 가 true 이면 " +
            "activeValidateProcessor 를 통과하고 CommitCheckModel 을 그대로 반환한다.")
    @Test
    void pass_the_process() throws Exception {
        CommitCheckModel commitCheckModel = new CommitCheckModel(
                1l,
                LocalDate.now(),
                LocalDateTime.now(),
                true,
                "jxxHxxName",
                "jxxHxxx",
                true);

        CommitCheckModel processedCommitCheckModel = activeValidateProcessor.process(commitCheckModel);
        assertThat(processedCommitCheckModel).isNotNull();
    }

    @DisplayName("CommitCheckModel 객체 필드 active 가 false 이면 " +
            "activeValidateProcessor 를 통과하지 못해 null 을 반환한다.")
    @Test
    void not_pass_the_process() throws Exception {
        CommitCheckModel commitCheckModel = new CommitCheckModel(
                1l,
                LocalDate.now(),
                LocalDateTime.now(),
                true,
                "jxxHxxName",
                "jxxHxxx",
                false);

        CommitCheckModel processedCommitCheckModel = activeValidateProcessor.process(commitCheckModel);
        assertThat(processedCommitCheckModel).isNull();
    }
}