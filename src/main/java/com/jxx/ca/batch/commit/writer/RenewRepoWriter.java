package com.jxx.ca.batch.commit.writer;

import com.jxx.ca.batch.commit.model.RenewRepoModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
public class RenewRepoWriter {

    private final DataSource dataSource;
    private final static String WRITE_SQL = "UPDATE " +
            "TODAY_COMMIT_MASTER " +
            "SET RECENTLY_PUSHED_REPO_NAME =:renewRepoName, " +
            "    RECENT_REPO_CHECK_DAY =:recentRepoCheckDay " +
            "WHERE GITHUB_MEMBER_PK =:githubMemberPk ";

    public JdbcBatchItemWriter<RenewRepoModel> build() {
        return new JdbcBatchItemWriterBuilder<RenewRepoModel>()
                .dataSource(dataSource)
                .sql(WRITE_SQL)
                .beanMapped()
                .build();
    }
}
