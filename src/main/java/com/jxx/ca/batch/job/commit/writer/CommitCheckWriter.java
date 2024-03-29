package com.jxx.ca.batch.job.commit.writer;

import com.jxx.ca.batch.job.commit.model.CommitCheckModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;

import javax.sql.DataSource;

import static com.jxx.ca.domain.TodayCommitTableSchema.*;

@Slf4j
@RequiredArgsConstructor
public class CommitCheckWriter {
    private final DataSource dataSource;
    private final static String WRITE_SQL = "UPDATE " +
            "TODAY_COMMIT_MASTER " +
            "SET DONE_CHECK_TIME=:checkTime, " +
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
