package com.jxx.ca.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Audited
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TODAY_COMMIT_MASTER")
public class TodayCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODAY_COMMIT_PK")
    private Long pk;

    @NotAudited
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GITHUB_MEMBER_PK", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private GithubMember githubMember;

    @Column(name = "CHECK_DAY")
    private LocalDate checkDay;
    @Column(name = "RECENTLY_PUSHED_REPO_NAME")
    private String recentlyPushedRepoName;

    @Column(name = "DONE")
    @Comment(value = "1=커밋함 0=커밋안함")
    @Convert(converter = BooleanToYnConverter.class)
    private Boolean done;
    @Column(name = "CHECK_TIME")
    private LocalDateTime checkTime;

    public TodayCommit(GithubMember githubMember, LocalDate checkDay, String recentlyPushedRepoName) {
        this.githubMember = githubMember;
        this.checkDay = checkDay;
        this.recentlyPushedRepoName = recentlyPushedRepoName;
        this.done = false;
        this.checkTime = null;
    }

    public void checkCommitDone(Boolean done) {
        this.done = done;
        checkTime = LocalDateTime.now();
    }


    public void initialize() {
        checkDay = LocalDate.now();
        done = false;
    }

}
