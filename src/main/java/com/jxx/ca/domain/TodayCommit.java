package com.jxx.ca.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TODAY_COMMIT_MASTER")
public class TodayCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODAY_COMMIT_PK")
    private Long pk;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GITHUB_MEMBER_PK", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private GithubMember githubMember;

    @Column(name = "TODAY")
    private LocalDate today;
    @Column(name = "RECENTLY_PUSHED_REPO_NAME")
    private String recentlyPushedRepoName;

    @Column(name = "DONE")
    @Comment(value = "1=커밋함 0=커밋안함")
    private Boolean done;
    @Column(name = "CHECK_TIME")
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
