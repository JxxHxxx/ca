package com.jxx.ca.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "JXX_GITHUB_MEMBER_MASTER", indexes = @Index(columnList = "GITHUB_NAME"))
public class GithubMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GITHUB_MEMBER_PK")
    private Long pk;

    @Column(name = "GITHUB_NAME", unique = true, nullable = false)
    @Comment(value = "깃헙 닉네임")
    private String githubName;

    @Column(name = "CREATED_TIME", nullable = false)
    @Comment(value = "생성일시")
    private LocalDateTime createdTime;

    @Column(name = "ACTIVE", nullable = false)
    @Comment(value = "사용자 활성화 여부 0=비활성화 1=활성화")
    private Boolean active;

    public GithubMember(String githubName) {
        this.githubName = githubName;
        createdTime = LocalDateTime.now();
        active = true;
    }

    public void inactive() {
        active = false;
    }
}
