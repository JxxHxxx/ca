package com.jxx.ca.batch.commit.reader;


import com.jxx.ca.domain.TodayCommit;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@RequiredArgsConstructor
public class CommitReader {

    private final DataSource dataSource;
    private final RowMapper<TodayCommitModel> rowMapper;

    private static final String READ_SQL = "SELECT * FROM TODAY_COMMIT_MASTER";

    public JdbcCursorItemReader<TodayCommitModel> build() {
        return new JdbcCursorItemReaderBuilder<TodayCommitModel>()
                .name("te")
                .dataSource(dataSource)
                .sql(READ_SQL) // SQL
                .rowMapper(rowMapper) // 레코드 -> 객체 변환
                .build();
    }
}
