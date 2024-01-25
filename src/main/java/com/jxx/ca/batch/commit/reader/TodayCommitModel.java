package com.jxx.ca.batch.commit.reader;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@RequiredArgsConstructor
public class TodayCommitModel {

    private final Long pk;
    private final Long githubMemberPk;
    private final LocalDate checkDay;
    private final String recentlyPushedRepoName;
    private final Boolean done;
    private final LocalDateTime checkTime;
}
