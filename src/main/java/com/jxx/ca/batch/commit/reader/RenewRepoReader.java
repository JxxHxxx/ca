package com.jxx.ca.batch.commit.reader;

import com.jxx.ca.batch.commit.model.RenewRepoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class RenewRepoReader {

    private final DataSource dataSource;
    private final RowMapper<RenewRepoModel> rowMapper;
    private static final String READ_SQL = "SELECT " +
            "JGMM.GITHUB_MEMBER_PK, " +
            "    TCM.RECENTLY_PUSHED_REPO_NAME, " +
            "    JGMM.GITHUB_NAME, " +
            "    JGMM.ACTIVE " +
            "FROM TODAY_COMMIT_MASTER TCM " +
            "JOIN JXX_GITHUB_MEMBER_MASTER JGMM ON TCM.GITHUB_MEMBER_PK = JGMM.GITHUB_MEMBER_PK";

    public JdbcCursorItemReader<RenewRepoModel> build() {
        return new JdbcCursorItemReaderBuilder<RenewRepoModel>()
                .name("commit-check.step1.reader")
                .dataSource(dataSource)
                .sql(READ_SQL)
                .rowMapper(rowMapper)
                .build();
    }
}
