package com.jxx.ca.batch.commit.job;


import com.jxx.ca.batch.commit.reader.CommitReader;
import com.jxx.ca.batch.commit.reader.TodayCommitModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CommitCheckJob  {
    private final DataSource dataSource;
    private final RowMapper<TodayCommitModel> rowMapper;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = "commit.check.job")
    public Job job() {
        return jobBuilderFactory.get("commit.check.job")
                .start(step())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(name = "commit.check.step1")
    public Step step() {
        return stepBuilderFactory.get("commit.check.step1")
                .<TodayCommitModel, TodayCommitModel>chunk(10)
                .reader(reader())
                .writer(writer())
                .build();
    }
    @Bean
    @StepScope
    public JdbcCursorItemReader<TodayCommitModel> reader() {
        return new CommitReader(dataSource, rowMapper)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<TodayCommitModel> writer() {
        return items -> {
            items.forEach(item -> log.info("item {}", item));
        };
    }

//    @Bean
//    @StepScope
//    public JdbcBatchItemWriter<String> writer() {
//        ItemWriter<Object> writer = new JdbcBatchItemWriter<>();
//        return null;
//    }
}
