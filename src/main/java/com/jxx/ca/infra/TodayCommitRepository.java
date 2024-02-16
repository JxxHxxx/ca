package com.jxx.ca.infra;

import com.jxx.ca.domain.TodayCommit;
import com.jxx.ca.dto.response.TodayCommitInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodayCommitRepository extends JpaRepository<TodayCommit, Long> {

    @Query("select new com.jxx.ca.dto.response.TodayCommitInformation(gm.githubName, tc.done)" +
            " from TodayCommit tc " +
            " join tc.githubMember gm " +
            " where gm.githubName like concat('%',:pattern ,'%') ")
    List<TodayCommitInformation> receiveTodayCommitInfoByGithubName(@Param("pattern") String pattern);
}
