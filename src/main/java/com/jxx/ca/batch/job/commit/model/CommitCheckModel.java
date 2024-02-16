package com.jxx.ca.batch.job.commit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class CommitCheckModel {

    private final Long todayCommitPk;
    private final LocalDate checkDay;
    private LocalDateTime checkTime;
    private Boolean done;
    private final String recentlyPushedRepoName;
    private final String githubName;
    private final Boolean active;

    public void checkCommitDone(Boolean done) {
        this.done = done;
        checkTime = LocalDateTime.now();
    }



    public boolean isActiveMember() {
        return active;
    }
}
