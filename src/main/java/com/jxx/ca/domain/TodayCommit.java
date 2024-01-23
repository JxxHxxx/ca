package com.jxx.ca.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODAY_COMMIT_PK")
    private Long pk;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GITHUB_MEMBER_PK", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private GithubMember githubMember;
    private LocalDate today;
    private String recentlyPushedRepoName;
    private Boolean done;
    private LocalDateTime checkTime;

    public TodayCommit(GithubMember githubMember, LocalDate today, String recentlyPushedRepoName) {
        this.githubMember = githubMember;
        this.today = today;
        this.recentlyPushedRepoName = recentlyPushedRepoName;
        this.done = false;
        this.checkTime = null;
    }

    public void setCheckTime(LocalDateTime checkTime) {
        this.checkTime = checkTime;
    }

    public void initialize() {
        today = LocalDate.now();
        done = false;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
