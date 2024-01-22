package com.jxx.ca;

import lombok.Getter;

@Getter
public class Member {

    private String name;
    private String lastUpdateRepoName;

    private Boolean todayCommit;

    public Member(String name, String lastUpdateRepoName, Boolean todayCommit) {
        this.name = name;
        this.lastUpdateRepoName = lastUpdateRepoName;
        this.todayCommit = todayCommit;
    }

    public void setLastUpdateRepoName(String repoName) {
        lastUpdateRepoName = repoName;
    }

    public void setTodayCommit(Boolean commit) {
        todayCommit = commit;
    }
}
