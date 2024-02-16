package com.jxx.ca.batch.job.commit.reader;

import com.jxx.ca.batch.job.commit.model.CommitCheckModel;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;


@RequiredArgsConstructor
public class CommitReader {

    private final DataSource dataSource;
    private final RowMapper<CommitCheckModel> rowMapper;
    private static final String READ_SQL =  "SELECT " +
            "TCM.TODAY_COMMIT_PK , " +
            "TCM.RECENT_REPO_CHECK_DAY , " +
            "TCM.COMMIT_DONE_CHECK_TIME , " +
            "TCM.DONE , " +
            "TCM.RECENTLY_PUSHED_REPO_NAME , " +
            "JGMM.GITHUB_NAME," +
            "JGMM.ACTIVE FROM TODAY_COMMIT_MASTER TCM " +
            "JOIN JXX_GITHUB_MEMBER_MASTER JGMM ON TCM.GITHUB_MEMBER_PK = JGMM.GITHUB_MEMBER_PK ";

    public JdbcCursorItemReader<CommitCheckModel> build() {
        return new JdbcCursorItemReaderBuilder<CommitCheckModel>()
                .name("commit-check.step2.reader")
                .dataSource(dataSource)
                .sql(READ_SQL) // SQL
                .rowMapper(rowMapper) // 레코드 -> 객체 변환
                .build();
    }
}
