package com.jxx.ca.batch.commit.reader;

import com.jxx.ca.domain.TodayCommit;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TodayCommitRowMapper implements RowMapper<TodayCommitModel> {

    @Override
    public TodayCommitModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        long pk = rs.getLong("TODAY_COMMIT_PK");
        long githubMemberPk = rs.getLong("GITHUB_MEMBER_PK");
        LocalDate checkDay = LocalDate.parse(rs.getString("CHECK_DAY"));
        String recentlyPushedRepoName = rs.getString("RECENTLY_PUSHED_REPO_NAME");
        Boolean done = "Y".equals(rs.getString("DONE"));
        String stringCheckTime = rs.getString("CHECK_TIME");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime checkTime = null;
        if (stringCheckTime != null) {
            checkTime = LocalDateTime.parse(rs.getString("CHECK_TIME"), formatter);
        }
        return new TodayCommitModel(pk, githubMemberPk, checkDay, recentlyPushedRepoName, done, checkTime);
    }
}
