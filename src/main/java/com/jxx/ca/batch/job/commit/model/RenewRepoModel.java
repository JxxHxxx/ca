package com.jxx.ca.batch.job.commit.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@ToString
@RequiredArgsConstructor
public class RenewRepoModel {

    private final Long githubMemberPk;
    private final boolean active;
    private final String githubName;
    private final String recentlyPushedRepoName; //최근 레포 네임
    private LocalDate recentRepoCheckDay;
    private String renewRepoName; // 갱신된 레포 네임

    public void renewRepoName(String renewRepoName) {
        this.renewRepoName = renewRepoName;
        this.recentRepoCheckDay = LocalDate.now();
    }

    public boolean isSameRecentlyPushedRepoName(String renewRepoName) {
        return Objects.equals(recentlyPushedRepoName, renewRepoName);
    }
}
