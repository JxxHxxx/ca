package com.jxx.ca.github.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}