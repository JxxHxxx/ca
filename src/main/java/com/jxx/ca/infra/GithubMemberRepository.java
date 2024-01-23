package com.jxx.ca.infra;

import com.jxx.ca.domain.GithubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubMemberRepository extends JpaRepository<GithubMember, Long> {
}
