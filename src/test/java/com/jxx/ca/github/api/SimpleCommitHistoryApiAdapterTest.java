package com.jxx.ca.github.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class SimpleCommitHistoryApiAdapterTest {

    @Autowired
    SimpleCommitHistoryApiAdapter simpleCommitHistoryApiAdapter;

    @Test
    void adjust_since_time() {
        //given
        String sinceTime = "2024-02-15T00:00:00Z";
        //when
        String adjustTime = simpleCommitHistoryApiAdapter.adjustSinceTime(sinceTime);
        //tehn
        assertThat(adjustTime).isEqualTo("2024-02-14T15:00Z");
    }

    @Test
    void time_print() {
        String suffix = "T00:00:00Z";
        LocalDate now = LocalDate.now(); // 현재 날짜와 시간 가져오기
        String format = String.valueOf(now) + suffix;
        System.out.println(format);
    }

}