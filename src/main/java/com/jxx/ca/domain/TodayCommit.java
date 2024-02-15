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

/**
 * 컬럼 명 변경 시
 * CommitReader,
 * CommitCheckWriter,
 * TodayCommitRowMapper
 * 클래스 직접 변경해야 함. 유지보수 한 곳에서 할 방법 고안
 */

@Audited
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TODAY_COMMIT_MASTER")
public class TodayCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODAY_COMMIT_PK")
    @Comment("TodayCommit PK")
    private Long pk;
    @NotAudited
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GITHUB_MEMBER_PK", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private GithubMember githubMember;
    @Column(name = "RECENT_REPO_CHECK_DAY")
    @Comment("최근에 업데이트 된 리포짓토리를 탐색한 시간")
    private LocalDate recentRepoCheckDay;
    @Column(name = "RECENTLY_PUSHED_REPO_NAME")
    @Comment("최근에 업데이트 된 리포짓토리 이름")
    private String recentlyPushedRepoName;
    @Column(name = "DONE")
    @Comment(value = "1=커밋함 0=커밋안함")
    private Boolean done;

    @Column(name = "COMMIT_DONE_CHECK_TIME")
    @Comment(value = "커밋 여부 체크 시간")
    private LocalDateTime commitDoneCheckTime;


    public TodayCommit(GithubMember githubMember, String recentlyPushedRepoName) {
        this.githubMember = githubMember;
        this.recentRepoCheckDay =  LocalDate.now();
        this.recentlyPushedRepoName = recentlyPushedRepoName;
        this.done = false;
        this.commitDoneCheckTime = null;

        this.githubMember.setTodayCommit(this);
    }

    public void checkCommitDone(Boolean done) {
        this.done = done;
        commitDoneCheckTime = LocalDateTime.now();
    }

    public void updateRecentlyPushedRepoName(String recentlyPushedRepoName) {
        this.recentRepoCheckDay = LocalDate.now();
        this.recentlyPushedRepoName = recentlyPushedRepoName;
    }

    public boolean isSameRecentlyPushedRepoName(String renewRepoName) {
        return recentlyPushedRepoName.equals(renewRepoName);
    }

    public void initialize() {
        recentRepoCheckDay = LocalDate.now();
        done = false;
    }

}
