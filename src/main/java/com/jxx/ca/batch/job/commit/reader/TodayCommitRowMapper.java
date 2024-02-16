package com.jxx.ca.batch.job.commit.reader;

import com.jxx.ca.batch.job.commit.model.CommitCheckModel;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TodayCommitRowMapper implements RowMapper<CommitCheckModel> {
    @Override
    public CommitCheckModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        long todayCommitPk = rs.getLong("TODAY_COMMIT_PK");
        LocalDate checkDay = LocalDate.parse(rs.getString("RECENT_REPO_CHECK_DAY"));
        String recentlyPushedRepoName = rs.getString("RECENTLY_PUSHED_REPO_NAME");

        boolean done = rs.getBoolean("DONE");
        String stringCheckTime = rs.getString("COMMIT_DONE_CHECK_TIME");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        LocalDateTime checkTime = null;
        if (stringCheckTime != null) {
            checkTime = LocalDateTime.parse(stringCheckTime, formatter);
        }

        String githubName = rs.getString("GITHUB_NAME");
        boolean active = rs.getBoolean("ACTIVE");

        return new CommitCheckModel(todayCommitPk, checkDay, checkTime, done, recentlyPushedRepoName,
                githubName, active);
    }
}
