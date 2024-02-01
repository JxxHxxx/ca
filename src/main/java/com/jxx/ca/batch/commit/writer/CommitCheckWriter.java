package com.jxx.ca.batch.commit.writer;

import com.jxx.ca.batch.commit.reader.CommitCheckModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
public class CommitCheckWriter {
    private final DataSource dataSource;
    private final static String WRITE_SQL = "UPDATE " +
            "TODAY_COMMIT_MASTER " +
            "SET RECENT_REPO_CHECK_DAY=:checkDay, " +
            "COMMIT_DONE_CHECK_TIME=:checkTime, " +
            "DONE=:done, " +
            "RECENTLY_PUSHED_REPO_NAME=:recentlyPushedRepoName " +
            "WHERE TODAY_COMMIT_PK=:todayCommitPk;";

    public JdbcBatchItemWriter<CommitCheckModel> build() {
        return new JdbcBatchItemWriterBuilder<CommitCheckModel>()
                .dataSource(dataSource)
                .sql(WRITE_SQL)
                .beanMapped()
                .build();
    }
}
