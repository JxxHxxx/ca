package com.jxx.ca.batch.commit.reader;

import com.jxx.ca.batch.commit.model.RenewRepoModel;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RenewRepoRowMapper implements RowMapper<RenewRepoModel> {
    @Override
    public RenewRepoModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        long githubMemberPk = rs.getLong("GITHUB_MEMBER_PK");
        boolean active = rs.getBoolean("ACTIVE");
        String githubName = rs.getString("GITHUB_NAME");
        String recentlyPushedRepoName = rs.getString("RECENTLY_PUSHED_REPO_NAME");
        return new RenewRepoModel(githubMemberPk, active, githubName, recentlyPushedRepoName);
    }
}
