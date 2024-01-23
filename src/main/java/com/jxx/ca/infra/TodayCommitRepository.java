package com.jxx.ca.infra;

import com.jxx.ca.domain.TodayCommit;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TodayCommitRepository extends JpaRepository<TodayCommit, Long> {
}
